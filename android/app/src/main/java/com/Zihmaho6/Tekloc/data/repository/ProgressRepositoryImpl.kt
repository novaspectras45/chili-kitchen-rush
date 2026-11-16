package com.Zihmaho6.Tekloc.data.repository

import com.Zihmaho6.Tekloc.core.config.GameConfig
import com.Zihmaho6.Tekloc.data.local.KitchenPreferences
import com.Zihmaho6.Tekloc.domain.model.KitchenProgress
import com.Zihmaho6.Tekloc.domain.model.ShiftResult
import com.Zihmaho6.Tekloc.domain.model.Station
import com.Zihmaho6.Tekloc.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressRepositoryImpl(
    private val preferences: KitchenPreferences
) : ProgressRepository {

    private val state = MutableStateFlow(preferences.loadProgress())

    override val progress: StateFlow<KitchenProgress> = state.asStateFlow()

    override fun applyShift(result: ShiftResult) {
        val current = state.value
        val stars = current.stars.toMutableMap()
        val best = current.bestScores.toMutableMap()
        val previousStars = stars[result.level] ?: 0
        if (result.stars > previousStars) stars[result.level] = result.stars
        val previousBest = best[result.level] ?: 0
        if (result.score > previousBest) best[result.level] = result.score
        val unlocked = if (result.cleared) {
            maxOf(current.unlockedLevels, minOf(result.level + 1, GameConfig.LEVEL_COUNT))
        } else {
            current.unlockedLevels
        }
        write(
            current.copy(
                coins = current.coins + result.coins,
                shifts = current.shifts + 1,
                plates = current.plates + result.plates,
                perfects = current.perfects + result.perfects,
                unlockedLevels = unlocked,
                lastLevel = result.level,
                stars = stars,
                bestScores = best
            )
        )
    }

    override fun markRecipe(recipeId: Int, perfect: Boolean) {
        val current = state.value
        val mastery = current.mastery.toMutableMap()
        val value = mastery[recipeId] ?: 0
        mastery[recipeId] = if (perfect) minOf(value + 1, GameConfig.MAX_TIER) else value
        write(current.copy(mastery = mastery))
    }

    override fun rememberLevel(level: Int) {
        val current = state.value
        if (current.lastLevel == level) return
        write(current.copy(lastLevel = level))
    }

    override fun upgrade(station: Station): Boolean {
        val current = state.value
        val tier = current.tiers.of(station)
        if (tier >= GameConfig.MAX_TIER) return false
        val cost = costOf(station, tier)
        if (current.coins < cost) return false
        write(
            current.copy(
                coins = current.coins - cost,
                tiers = current.tiers.with(station, tier + 1)
            )
        )
        return true
    }

    override fun reset() {
        preferences.clearProgress()
        state.value = preferences.loadProgress()
    }

    private fun write(value: KitchenProgress) {
        state.value = value
        preferences.saveProgress(value)
    }

    private fun costOf(station: Station, tier: Int): Int = when (station) {
        Station.BURNER -> GameConfig.BURNER_COSTS.getOrElse(tier) { Int.MAX_VALUE }
        Station.COOLING_RACK -> GameConfig.COOLING_COSTS.getOrElse(tier) { Int.MAX_VALUE }
        Station.PREP_TABLE -> GameConfig.PREP_COSTS.getOrElse(tier) { Int.MAX_VALUE }
    }
}
