package com.Zihmaho6.Tekloc.core.navigation

import com.Zihmaho6.Tekloc.domain.model.ShiftResult

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data object LevelSelect : Screen

    data class Game(val level: Int, val attempt: Int) : Screen

    data class Result(val shift: ShiftResult) : Screen

    data object Collection : Screen

    data object Stats : Screen

    data object Settings : Screen
}
