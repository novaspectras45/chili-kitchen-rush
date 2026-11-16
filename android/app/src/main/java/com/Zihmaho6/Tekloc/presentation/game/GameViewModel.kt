package com.Zihmaho6.Tekloc.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zihmaho6.Tekloc.core.config.GameConfig
import com.Zihmaho6.Tekloc.data.sample.SampleData
import com.Zihmaho6.Tekloc.domain.model.LevelSpec
import com.Zihmaho6.Tekloc.domain.model.OrderTicket
import com.Zihmaho6.Tekloc.domain.model.ShiftOutcome
import com.Zihmaho6.Tekloc.domain.model.ShiftResult
import com.Zihmaho6.Tekloc.domain.repository.ProgressRepository
import com.Zihmaho6.Tekloc.domain.repository.SettingsRepository
import com.Zihmaho6.Tekloc.domain.usecase.GenerateShiftUseCase
import com.Zihmaho6.Tekloc.domain.usecase.ResolveServeUseCase
import com.Zihmaho6.Tekloc.domain.usecase.ScoreShiftUseCase
import com.Zihmaho6.Tekloc.domain.usecase.ServeKind
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val generateShift: GenerateShiftUseCase,
    private val resolveServe: ResolveServeUseCase,
    private val scoreShift: ScoreShiftUseCase
) : ViewModel() {

    private val mutable = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = mutable.asStateFlow()

    private val finishedChannel = Channel<ShiftResult>(Channel.BUFFERED)
    val finished: Flow<ShiftResult> = finishedChannel.receiveAsFlow()

    private var loopJob: Job? = null
    private var resolveJob: Job? = null

    private var spec: LevelSpec = SampleData.level(1)
    private var deck: List<OrderTicket> = emptyList()
    private var deckIndex = 0
    private var burnerStep = GameConfig.BURNER_STEPS.first()
    private var coolingStep = GameConfig.COOLING_STEPS.first()
    private var preparedKey = -1

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                mutable.update {
                    it.copy(
                        leftHanded = settings.leftHanded,
                        numericTimer = settings.numericTimer,
                        highContrast = settings.highContrast,
                        reduceMotion = settings.reduceMotion
                    )
                }
            }
        }
    }

    fun prepare(level: Int, attempt: Int) {
        val key = level * 1000 + attempt
        if (preparedKey == key) return
        preparedKey = key
        loopJob?.cancel()
        loopJob = null
        resolveJob?.cancel()
        resolveJob = null

        spec = SampleData.level(level)
        val tiers = progressRepository.progress.value.tiers
        burnerStep = GameConfig.BURNER_STEPS[tiers.burner.coerceIn(0, GameConfig.MAX_TIER)]
        coolingStep = GameConfig.COOLING_STEPS[tiers.cooling.coerceIn(0, GameConfig.MAX_TIER)]
        val windowBonus = GameConfig.PREP_WINDOW_BONUS[tiers.prep.coerceIn(0, GameConfig.MAX_TIER)]
        deck = generateShift(spec, attempt, SampleData.recipes, windowBonus)
        deckIndex = 0

        val settings = settingsRepository.settings.value
        mutable.value = GameUiState(
            phase = GamePhase.IDLE,
            level = spec.index,
            band = spec.band,
            quota = spec.quota,
            hearts = GameConfig.MAX_HEARTS,
            heat = 0f,
            window = spec.tolerance + windowBonus,
            shiftRemaining = spec.shiftSeconds.toFloat(),
            shiftTotal = spec.shiftSeconds.toFloat(),
            rail = emptyList(),
            leftHanded = settings.leftHanded,
            numericTimer = settings.numericTimer,
            highContrast = settings.highContrast,
            reduceMotion = settings.reduceMotion
        )
    }

    fun go() {
        if (mutable.value.phase != GamePhase.IDLE) return
        val opening = ArrayList<RailTicket>()
        while (opening.size < GameConfig.RAIL_SIZE) {
            val ticket = nextTicket() ?: break
            opening.add(ticket)
        }
        mutable.update { it.copy(phase = GamePhase.PLAYING, rail = opening) }
        startLoop()
    }

    fun heatUp() = adjust(burnerStep)

    fun coolDown() = adjust(-coolingStep)

    fun serve() {
        val current = mutable.value
        if (current.phase != GamePhase.PLAYING) return
        val ticket = current.rail.firstOrNull() ?: return
        val outcome = resolveServe(
            heat = current.heat,
            targetHeat = ticket.targetHeat,
            rush = ticket.rush,
            level = current.level,
            window = current.window,
            combo = current.combo
        )
        if (!outcome.accepted) {
            val hearts = current.hearts - 1
            mutable.update {
                it.copy(hearts = hearts, combo = 0, failToken = it.failToken + 1)
            }
            if (hearts <= 0) finish(ShiftOutcome.HEARTS_LOST)
            return
        }

        val perfect = outcome.kind == ServeKind.PERFECT
        progressRepository.markRecipe(ticket.recipeId, perfect)
        val refilled = current.rail.drop(1) + listOfNotNull(nextTicket())
        val combo = current.combo + 1
        val plates = current.plates + 1
        mutable.update {
            it.copy(
                phase = GamePhase.RESOLVING,
                plates = plates,
                perfects = it.perfects + if (perfect) 1 else 0,
                combo = combo,
                bestCombo = maxOf(it.bestCombo, combo),
                coins = it.coins + outcome.coins,
                score = it.score + outcome.points,
                rail = refilled,
                lastPoints = outcome.points,
                gainToken = it.gainToken + 1
            )
        }
        if (plates >= spec.quota) {
            finish(ShiftOutcome.QUOTA_MET)
            return
        }
        resolveJob?.cancel()
        resolveJob = viewModelScope.launch {
            delay(GameConfig.RESOLVE_MS)
            mutable.update {
                if (it.phase == GamePhase.RESOLVING) it.copy(phase = GamePhase.PLAYING) else it
            }
        }
    }

    fun pause() {
        val phase = mutable.value.phase
        if (phase != GamePhase.PLAYING && phase != GamePhase.RESOLVING) return
        loopJob?.cancel()
        loopJob = null
        resolveJob?.cancel()
        resolveJob = null
        mutable.update { it.copy(phase = GamePhase.PAUSED) }
    }

    fun resume() {
        if (mutable.value.phase != GamePhase.PAUSED) return
        mutable.update { it.copy(phase = GamePhase.PLAYING) }
        startLoop()
    }

    fun abandon() {
        loopJob?.cancel()
        loopJob = null
        resolveJob?.cancel()
        resolveJob = null
        preparedKey = -1
    }

    private fun adjust(delta: Float) {
        val phase = mutable.value.phase
        if (phase != GamePhase.PLAYING && phase != GamePhase.RESOLVING) return
        mutable.update {
            it.copy(
                heat = (it.heat + delta).coerceIn(GameConfig.HEAT_MIN, GameConfig.HEAT_MAX),
                pressToken = it.pressToken + 1
            )
        }
    }

    private fun startLoop() {
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            while (isActive) {
                delay(GameConfig.TICK_MS)
                tick()
            }
        }
    }

    private fun tick() {
        val current = mutable.value
        if (current.phase != GamePhase.PLAYING && current.phase != GamePhase.RESOLVING) return
        val step = GameConfig.TICK_SECONDS
        val remaining = current.shiftRemaining - step
        if (remaining <= 0f) {
            mutable.update { it.copy(shiftRemaining = 0f) }
            finish(ShiftOutcome.TIMER_OUT)
            return
        }
        val heat = (current.heat - GameConfig.HEAT_DRIFT_PER_SECOND * step)
            .coerceAtLeast(GameConfig.HEAT_MIN)
        var rail = current.rail
        var combo = current.combo
        val head = rail.firstOrNull()
        if (head != null && head.rush) {
            val left = head.rushRemaining - step
            if (left <= 0f) {
                rail = rail.drop(1) + listOfNotNull(nextTicket())
                combo = 0
            } else {
                val updated = rail.toMutableList()
                updated[0] = head.copy(rushRemaining = left)
                rail = updated
            }
        }
        if (rail.isEmpty()) {
            mutable.update { it.copy(shiftRemaining = remaining, heat = heat, rail = rail) }
            finish(
                if (current.plates >= spec.quota) ShiftOutcome.QUOTA_MET else ShiftOutcome.TIMER_OUT
            )
            return
        }
        val nextRail = rail
        val nextCombo = combo
        mutable.update {
            it.copy(shiftRemaining = remaining, heat = heat, rail = nextRail, combo = nextCombo)
        }
    }

    private fun finish(outcome: ShiftOutcome) {
        loopJob?.cancel()
        loopJob = null
        resolveJob?.cancel()
        resolveJob = null
        val current = mutable.value
        if (current.phase == GamePhase.FINISHED) return
        val previousBest = progressRepository.progress.value.bestScores[current.level] ?: 0
        val result = scoreShift(
            spec = spec,
            outcome = outcome,
            plates = current.plates,
            perfects = current.perfects,
            bestCombo = current.bestCombo,
            coins = current.coins,
            score = current.score,
            heartsLeft = current.hearts,
            previousBest = previousBest
        )
        progressRepository.applyShift(result)
        mutable.update { it.copy(phase = GamePhase.FINISHED) }
        preparedKey = -1
        viewModelScope.launch { finishedChannel.send(result) }
    }

    private fun nextTicket(): RailTicket? {
        if (deckIndex >= deck.size) return null
        val ticket = deck[deckIndex]
        deckIndex++
        return RailTicket(
            id = ticket.id,
            recipeId = ticket.recipeId,
            nameRes = SampleData.recipe(ticket.recipeId).nameRes,
            targetHeat = ticket.targetHeat,
            pips = ticket.targetHeat.roundToInt().coerceIn(1, 10),
            rush = ticket.rush,
            rushRemaining = ticket.rushRemaining
        )
    }
}
