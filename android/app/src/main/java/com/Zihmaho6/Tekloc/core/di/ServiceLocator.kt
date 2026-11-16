package com.Zihmaho6.Tekloc.core.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.Zihmaho6.Tekloc.data.local.KitchenPreferences
import com.Zihmaho6.Tekloc.data.repository.ProgressRepositoryImpl
import com.Zihmaho6.Tekloc.data.repository.SettingsRepositoryImpl
import com.Zihmaho6.Tekloc.domain.usecase.GenerateShiftUseCase
import com.Zihmaho6.Tekloc.domain.usecase.ResolveServeUseCase
import com.Zihmaho6.Tekloc.domain.usecase.ScoreShiftUseCase

object ServiceLocator {

    private var cachedFactory: ViewModelProvider.Factory? = null

    fun init(context: Context) {
        if (cachedFactory != null) return
        val preferences = KitchenPreferences(context.applicationContext)
        cachedFactory = AppViewModelFactory.build(
            progressRepository = ProgressRepositoryImpl(preferences),
            settingsRepository = SettingsRepositoryImpl(preferences),
            generateShift = GenerateShiftUseCase(),
            resolveServe = ResolveServeUseCase(),
            scoreShift = ScoreShiftUseCase()
        )
    }

    val factory: ViewModelProvider.Factory
        get() = cachedFactory ?: error("ServiceLocator.init must run before the first ViewModel")
}
