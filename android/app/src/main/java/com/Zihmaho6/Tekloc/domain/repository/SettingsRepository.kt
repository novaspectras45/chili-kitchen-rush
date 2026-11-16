package com.Zihmaho6.Tekloc.domain.repository

import com.Zihmaho6.Tekloc.domain.model.GameSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    val settings: StateFlow<GameSettings>

    fun update(settings: GameSettings)
}
