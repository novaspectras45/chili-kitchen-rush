package com.Zihmaho6.Tekloc.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zihmaho6.Tekloc.R
import com.Zihmaho6.Tekloc.core.config.GameConfig
import com.Zihmaho6.Tekloc.domain.model.KitchenProgress
import com.Zihmaho6.Tekloc.domain.model.Station
import com.Zihmaho6.Tekloc.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatsViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val mutable = MutableStateFlow(StatsUiState())
    val state: StateFlow<StatsUiState> = mutable.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                mutable.value = StatsUiState(
                    coins = progress.coins,
                    shifts = progress.shifts,
                    plates = progress.plates,
                    perfectRate = progress.perfectRate,
                    stars = progress.totalStars,
                    stations = rows(progress)
                )
            }
        }
    }

    fun upgrade(station: Station) {
        progressRepository.upgrade(station)
    }

    private fun rows(progress: KitchenProgress): List<StationRow> = listOf(
        row(
            station = Station.BURNER,
            nameRes = R.string.station_burner,
            effectRes = R.string.station_burner_effect,
            values = GameConfig.BURNER_STEPS,
            costs = GameConfig.BURNER_COSTS,
            tier = progress.tiers.burner,
            coins = progress.coins
        ),
        row(
            station = Station.COOLING_RACK,
            nameRes = R.string.station_cooling,
            effectRes = R.string.station_cooling_effect,
            values = GameConfig.COOLING_STEPS,
            costs = GameConfig.COOLING_COSTS,
            tier = progress.tiers.cooling,
            coins = progress.coins
        ),
        row(
            station = Station.PREP_TABLE,
            nameRes = R.string.station_prep,
            effectRes = R.string.station_prep_effect,
            values = GameConfig.PREP_WINDOW_BONUS,
            costs = GameConfig.PREP_COSTS,
            tier = progress.tiers.prep,
            coins = progress.coins
        )
    )

    private fun row(
        station: Station,
        nameRes: Int,
        effectRes: Int,
        values: List<Float>,
        costs: List<Int>,
        tier: Int,
        coins: Int
    ): StationRow {
        val safeTier = tier.coerceIn(0, GameConfig.MAX_TIER)
        val maxed = safeTier >= GameConfig.MAX_TIER
        val cost = if (maxed) 0 else costs[safeTier]
        return StationRow(
            station = station,
            nameRes = nameRes,
            effectRes = effectRes,
            effectValue = values[safeTier],
            tier = safeTier,
            cost = cost,
            affordable = !maxed && coins >= cost,
            maxed = maxed
        )
    }
}
