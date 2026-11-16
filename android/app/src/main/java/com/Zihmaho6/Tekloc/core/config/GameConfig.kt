package com.Zihmaho6.Tekloc.core.config

object GameConfig {

    const val LOADER_DURATION_MS = 8000L

    const val TICK_MS = 16L
    const val TICK_SECONDS = 0.016f

    const val HEAT_MIN = 0f
    const val HEAT_MAX = 10f
    const val HEAT_DRIFT_PER_SECOND = 0.25f
    const val PERFECT_WINDOW = 0.25f

    const val MAX_HEARTS = 3
    const val RAIL_SIZE = 3
    const val RUSH_EVERY = 4
    const val RUSH_SECONDS = 8f
    const val RESOLVE_MS = 600L

    const val LEVEL_COUNT = 12
    const val RECIPE_COUNT = 12
    const val MAX_TIER = 3

    const val COMBO_BONUS = 0.15f
    const val COINS_PER_POINT = 10

    val BURNER_STEPS = listOf(1.4f, 1.6f, 1.8f, 2.0f)
    val COOLING_STEPS = listOf(1.0f, 1.2f, 1.45f, 1.7f)
    val PREP_WINDOW_BONUS = listOf(0f, 0.15f, 0.3f, 0.5f)

    val BURNER_COSTS = listOf(120, 300, 640)
    val COOLING_COSTS = listOf(120, 300, 640)
    val PREP_COSTS = listOf(160, 380, 760)
}
