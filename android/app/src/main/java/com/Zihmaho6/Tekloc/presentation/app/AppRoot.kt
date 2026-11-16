package com.Zihmaho6.Tekloc.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zihmaho6.Tekloc.core.di.ServiceLocator
import com.Zihmaho6.Tekloc.core.navigation.Screen
import com.Zihmaho6.Tekloc.presentation.collection.CollectionRoute
import com.Zihmaho6.Tekloc.presentation.game.GameRoute
import com.Zihmaho6.Tekloc.presentation.levels.LevelSelectRoute
import com.Zihmaho6.Tekloc.presentation.menu.MenuRoute
import com.Zihmaho6.Tekloc.presentation.result.ResultRoute
import com.Zihmaho6.Tekloc.presentation.settings.SettingsRoute
import com.Zihmaho6.Tekloc.presentation.splash.SplashRoute
import com.Zihmaho6.Tekloc.presentation.stats.StatsRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()

    BackHandler(enabled = screen !is Screen.Menu && screen !is Screen.Splash) {
        appViewModel.back()
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            fadeIn(tween(durationMillis = 320)) togetherWith fadeOut(tween(durationMillis = 260))
        },
        modifier = Modifier.fillMaxSize(),
        label = "screenHost"
    ) { target ->
        when (target) {
            is Screen.Splash -> SplashRoute(
                onFinished = { appViewModel.openMenu() }
            )

            is Screen.Menu -> MenuRoute(
                onPlay = { level -> appViewModel.launchFromMenu(level) },
                onLevels = { appViewModel.open(Screen.LevelSelect) },
                onStats = { appViewModel.open(Screen.Stats) },
                onCollection = { appViewModel.open(Screen.Collection) },
                onSettings = { appViewModel.open(Screen.Settings) }
            )

            is Screen.LevelSelect -> LevelSelectRoute(
                onBack = { appViewModel.back() },
                onPickLevel = { level -> appViewModel.launchFromRoute(level) }
            )

            is Screen.Game -> GameRoute(
                level = target.level,
                attempt = target.attempt,
                onFinished = { result -> appViewModel.showResult(Screen.Result(result)) },
                onMenu = { appViewModel.openMenu() },
                onSettings = { appViewModel.open(Screen.Settings) }
            )

            is Screen.Result -> ResultRoute(
                shift = target.shift,
                onPlayAgain = { appViewModel.launchFromRoute(target.shift.level) },
                onMenu = { appViewModel.openMenu() },
                onCollection = { appViewModel.open(Screen.Collection) }
            )

            is Screen.Collection -> CollectionRoute(
                onBack = { appViewModel.back() }
            )

            is Screen.Stats -> StatsRoute(
                onBack = { appViewModel.back() }
            )

            is Screen.Settings -> SettingsRoute(
                onBack = { appViewModel.back() }
            )
        }
    }
}
