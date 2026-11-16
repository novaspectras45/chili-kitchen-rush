package com.Zihmaho6.Tekloc.presentation.settings

data class SettingsUiState(
    val leftHanded: Boolean = false,
    val numericTimer: Boolean = false,
    val highContrast: Boolean = false,
    val reduceMotion: Boolean = false,
    val confirmReset: Boolean = false,
    val coins: Int = 0
)
