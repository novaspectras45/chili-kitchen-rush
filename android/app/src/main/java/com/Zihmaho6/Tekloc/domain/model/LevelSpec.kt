package com.Zihmaho6.Tekloc.domain.model

enum class ToleranceBand { WIDE, TIGHT, RAZOR }

data class LevelSpec(
    val index: Int,
    val quota: Int,
    val shiftSeconds: Int,
    val tolerance: Float,
    val heatLow: Float,
    val heatHigh: Float,
    val band: ToleranceBand
)
