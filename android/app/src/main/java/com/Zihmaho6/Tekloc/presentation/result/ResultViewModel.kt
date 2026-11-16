package com.Zihmaho6.Tekloc.presentation.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zihmaho6.Tekloc.domain.model.ShiftResult
import com.Zihmaho6.Tekloc.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val mutable = MutableStateFlow(ResultUiState())
    val state: StateFlow<ResultUiState> = mutable.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                mutable.update { it.copy(walletCoins = progress.coins) }
            }
        }
    }

    fun present(shift: ShiftResult) {
        mutable.update {
            it.copy(
                outcome = shift.outcome,
                level = shift.level,
                plates = shift.plates,
                quota = shift.quota,
                perfects = shift.perfects,
                bestCombo = shift.bestCombo,
                coins = shift.coins,
                score = shift.score,
                stars = shift.stars,
                newRecord = shift.newRecord
            )
        }
    }
}
