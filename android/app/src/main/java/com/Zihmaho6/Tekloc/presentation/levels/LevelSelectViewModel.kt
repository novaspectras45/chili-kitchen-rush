package com.Zihmaho6.Tekloc.presentation.levels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zihmaho6.Tekloc.data.sample.SampleData
import com.Zihmaho6.Tekloc.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LevelSelectViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val mutable = MutableStateFlow(LevelSelectUiState())
    val state: StateFlow<LevelSelectUiState> = mutable.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                val cards = SampleData.levels.map { spec ->
                    LevelCard(
                        index = spec.index,
                        unlocked = spec.index <= progress.unlockedLevels,
                        stars = progress.stars[spec.index] ?: 0,
                        band = spec.band
                    )
                }
                mutable.update { it.copy(coins = progress.coins, levels = cards) }
            }
        }
    }
}
