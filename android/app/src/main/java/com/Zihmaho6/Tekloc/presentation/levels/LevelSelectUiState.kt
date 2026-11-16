package com.Zihmaho6.Tekloc.presentation.levels

import com.Zihmaho6.Tekloc.domain.model.ToleranceBand

data class LevelCard(
    val index: Int,
    val unlocked: Boolean,
    val stars: Int,
    val band: ToleranceBand
)

data class LevelSelectUiState(
    val coins: Int = 0,
    val levels: List<LevelCard> = emptyList()
)
