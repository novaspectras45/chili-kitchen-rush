package com.Zihmaho6.Tekloc.domain.usecase

import com.Zihmaho6.Tekloc.core.config.GameConfig
import kotlin.math.abs
import kotlin.math.roundToInt

enum class ServeKind { PERFECT, SERVED, SENT_BACK }

data class ServeOutcome(
    val kind: ServeKind,
    val points: Int,
    val coins: Int
) {
    val accepted: Boolean get() = kind != ServeKind.SENT_BACK
}

class ResolveServeUseCase {

    operator fun invoke(
        heat: Float,
        targetHeat: Float,
        rush: Boolean,
        level: Int,
        window: Float,
        combo: Int
    ): ServeOutcome {
        val distance = abs(heat - targetHeat)
        val multiplier = 1f + combo * GameConfig.COMBO_BONUS
        val rushFactor = if (rush) 2 else 1
        return when {
            distance <= GameConfig.PERFECT_WINDOW ->
                award(ServeKind.PERFECT, (100 * level + 120) * multiplier, rushFactor)
            distance <= window ->
                award(ServeKind.SERVED, (100 * level).toFloat() * multiplier, rushFactor)
            else -> ServeOutcome(ServeKind.SENT_BACK, 0, 0)
        }
    }

    private fun award(kind: ServeKind, raw: Float, rushFactor: Int): ServeOutcome {
        val points = raw.roundToInt() * rushFactor
        return ServeOutcome(kind, points, points / GameConfig.COINS_PER_POINT)
    }
}
