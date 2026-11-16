package com.Zihmaho6.Tekloc.core.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.Zihmaho6.Tekloc.domain.repository.ProgressRepository
import com.Zihmaho6.Tekloc.domain.repository.SettingsRepository
import com.Zihmaho6.Tekloc.domain.usecase.GenerateShiftUseCase
import com.Zihmaho6.Tekloc.domain.usecase.ResolveServeUseCase
import com.Zihmaho6.Tekloc.domain.usecase.ScoreShiftUseCase
import com.Zihmaho6.Tekloc.presentation.app.AppViewModel
import com.Zihmaho6.Tekloc.presentation.collection.CollectionViewModel
import com.Zihmaho6.Tekloc.presentation.game.GameViewModel
import com.Zihmaho6.Tekloc.presentation.levels.LevelSelectViewModel
import com.Zihmaho6.Tekloc.presentation.menu.MenuViewModel
import com.Zihmaho6.Tekloc.presentation.result.ResultViewModel
import com.Zihmaho6.Tekloc.presentation.settings.SettingsViewModel
import com.Zihmaho6.Tekloc.presentation.splash.SplashViewModel
import com.Zihmaho6.Tekloc.presentation.stats.StatsViewModel

object AppViewModelFactory {

    fun build(
        progressRepository: ProgressRepository,
        settingsRepository: SettingsRepository,
        generateShift: GenerateShiftUseCase,
        resolveServe: ResolveServeUseCase,
        scoreShift: ScoreShiftUseCase
    ): ViewModelProvider.Factory = viewModelFactory {
        initializer { AppViewModel(progressRepository) }
        initializer { SplashViewModel(settingsRepository) }
        initializer { MenuViewModel(progressRepository, settingsRepository) }
        initializer { LevelSelectViewModel(progressRepository) }
        initializer {
            GameViewModel(
                progressRepository,
                settingsRepository,
                generateShift,
                resolveServe,
                scoreShift
            )
        }
        initializer { ResultViewModel(progressRepository) }
        initializer { CollectionViewModel(progressRepository) }
        initializer { StatsViewModel(progressRepository) }
        initializer { SettingsViewModel(settingsRepository, progressRepository) }
    }
}
