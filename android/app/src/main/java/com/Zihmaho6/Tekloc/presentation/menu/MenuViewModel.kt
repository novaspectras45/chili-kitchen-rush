package com.Zihmaho6.Tekloc.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zihmaho6.Tekloc.core.config.GameConfig
import com.Zihmaho6.Tekloc.domain.repository.ProgressRepository
import com.Zihmaho6.Tekloc.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val mutable = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = mutable.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                mutable.value = mutable.value.copy(
                    coins = progress.coins,
                    nextLevel = progress.lastLevel.coerceIn(1, GameConfig.LEVEL_COUNT),
                    unlockedLevels = progress.unlockedLevels,
                    levelCount = GameConfig.LEVEL_COUNT,
                    unlockedRecipes = progress.unlockedRecipes,
                    recipeCount = GameConfig.RECIPE_COUNT,
                    topTier = progress.topTier,
                    bestScore = progress.bestScores.values.maxOrNull() ?: 0
                )
            }
        }
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                mutable.value = mutable.value.copy(reduceMotion = settings.reduceMotion)
            }
        }
    }
}
