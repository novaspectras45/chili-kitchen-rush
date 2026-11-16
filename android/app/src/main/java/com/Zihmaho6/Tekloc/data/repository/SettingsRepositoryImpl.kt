package com.Zihmaho6.Tekloc.data.repository

import com.Zihmaho6.Tekloc.data.local.KitchenPreferences
import com.Zihmaho6.Tekloc.domain.model.GameSettings
import com.Zihmaho6.Tekloc.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(
    private val preferences: KitchenPreferences
) : SettingsRepository {

    private val state = MutableStateFlow(preferences.loadSettings())

    override val settings: StateFlow<GameSettings> = state.asStateFlow()

    override fun update(settings: GameSettings) {
        state.value = settings
        preferences.saveSettings(settings)
    }
}
