package com.Zihmaho6.Tekloc.presentation.result

import com.Zihmaho6.Tekloc.domain.model.ShiftOutcome

data class ResultUiState(
    val outcome: ShiftOutcome = ShiftOutcome.TIMER_OUT,
    val level: Int = 1,
    val plates: Int = 0,
    val quota: Int = 0,
    val perfects: Int = 0,
    val bestCombo: Int = 0,
    val coins: Int = 0,
    val score: Int = 0,
    val stars: Int = 0,
    val newRecord: Boolean = false,
    val walletCoins: Int = 0
) {
    val cleared: Boolean get() = outcome == ShiftOutcome.QUOTA_MET
}
