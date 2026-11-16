package com.Zihmaho6.Tekloc.domain.model

enum class ShiftOutcome { QUOTA_MET, TIMER_OUT, HEARTS_LOST }

data class ShiftResult(
    val level: Int,
    val outcome: ShiftOutcome,
    val plates: Int,
    val quota: Int,
    val perfects: Int,
    val bestCombo: Int,
    val coins: Int,
    val score: Int,
    val stars: Int,
    val newRecord: Boolean
) {
    val cleared: Boolean get() = outcome == ShiftOutcome.QUOTA_MET
}
