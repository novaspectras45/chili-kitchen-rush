package com.Zihmaho6.Tekloc.presentation.stats

import com.Zihmaho6.Tekloc.domain.model.Station

data class StationRow(
    val station: Station,
    val nameRes: Int,
    val effectRes: Int,
    val effectValue: Float,
    val tier: Int,
    val cost: Int,
    val affordable: Boolean,
    val maxed: Boolean
)

data class StatsUiState(
    val coins: Int = 0,
    val shifts: Int = 0,
    val plates: Int = 0,
    val perfectRate: Int = 0,
    val stars: Int = 0,
    val stations: List<StationRow> = emptyList()
)
