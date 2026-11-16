package com.Zihmaho6.Tekloc.presentation.menu

data class MenuUiState(
    val coins: Int = 0,
    val nextLevel: Int = 1,
    val unlockedLevels: Int = 1,
    val levelCount: Int = 12,
    val unlockedRecipes: Int = 0,
    val recipeCount: Int = 12,
    val topTier: Int = 0,
    val bestScore: Int = 0,
    val reduceMotion: Boolean = false
)
