package com.Zihmaho6.Tekloc.presentation.game

import com.Zihmaho6.Tekloc.domain.model.ToleranceBand

enum class GamePhase { IDLE, PLAYING, RESOLVING, PAUSED, FINISHED }

data class RailTicket(
    val id: Int,
    val recipeId: Int,
    val nameRes: Int,
    val targetHeat: Float,
    val pips: Int,
    val rush: Boolean,
    val rushRemaining: Float
)

data class GameUiState(
    val phase: GamePhase = GamePhase.IDLE,
    val level: Int = 1,
    val band: ToleranceBand = ToleranceBand.WIDE,
    val quota: Int = 6,
    val plates: Int = 0,
    val perfects: Int = 0,
    val hearts: Int = 3,
    val combo: Int = 0,
    val bestCombo: Int = 0,
    val coins: Int = 0,
    val score: Int = 0,
    val heat: Float = 0f,
    val window: Float = 1f,
    val shiftRemaining: Float = 75f,
    val shiftTotal: Float = 75f,
    val rail: List<RailTicket> = emptyList(),
    val lastPoints: Int = 0,
    val pressToken: Int = 0,
    val gainToken: Int = 0,
    val failToken: Int = 0,
    val leftHanded: Boolean = false,
    val numericTimer: Boolean = false,
    val highContrast: Boolean = false,
    val reduceMotion: Boolean = false
) {
    val active: RailTicket? get() = rail.firstOrNull()

    val targetHeat: Float get() = active?.targetHeat ?: 0f

    val running: Boolean get() = phase == GamePhase.PLAYING

    val canServe: Boolean get() = phase == GamePhase.PLAYING && rail.isNotEmpty()
}
