package com.Zihmaho6.Tekloc.domain.model

data class GameSettings(
    val leftHanded: Boolean = false,
    val numericTimer: Boolean = false,
    val highContrast: Boolean = false,
    val reduceMotion: Boolean = false
)
