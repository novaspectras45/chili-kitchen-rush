package com.Zihmaho6.Tekloc.domain.usecase

import com.Zihmaho6.Tekloc.core.config.GameConfig
import com.Zihmaho6.Tekloc.domain.model.LevelSpec
import com.Zihmaho6.Tekloc.domain.model.OrderTicket
import com.Zihmaho6.Tekloc.domain.model.Recipe
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

class GenerateShiftUseCase {

    operator fun invoke(
        spec: LevelSpec,
        attempt: Int,
        recipes: List<Recipe>,
        windowBonus: Float
    ): List<OrderTicket> {
        if (recipes.isEmpty()) return emptyList()
        val seed = spec.index * 7919 xor (attempt * 104729)
        val random = Random(seed)
        val window = spec.tolerance + windowBonus
        val deck = ArrayList<OrderTicket>()
        val total = spec.quota * 2 + GameConfig.RAIL_SIZE
        var previous = 0f
        var id = 1
        while (deck.size < total) {
            var target = fallbackTarget(spec, deck.size)
            var attemptsLeft = 20
            while (attemptsLeft > 0) {
                val candidate = roundToTenth(spec.heatLow + random.nextFloat() * (spec.heatHigh - spec.heatLow))
                if (solvable(previous, candidate, window)) {
                    target = candidate
                    break
                }
                attemptsLeft--
            }
            val rush = id % GameConfig.RUSH_EVERY == 0
            val recipe = recipes[random.nextInt(recipes.size)]
            deck.add(
                OrderTicket(
                    id = id,
                    recipeId = recipe.id,
                    targetHeat = target,
                    rush = rush,
                    rushRemaining = if (rush) GameConfig.RUSH_SECONDS else 0f
                )
            )
            previous = target
            id++
        }
        return deck
    }

    private fun solvable(previous: Float, candidate: Float, window: Float): Boolean {
        if (candidate < GameConfig.HEAT_MIN || candidate > GameConfig.HEAT_MAX) return false
        val distance = abs(candidate - previous)
        return distance >= window * 1.2f && distance <= 6f
    }

    private fun fallbackTarget(spec: LevelSpec, index: Int): Float {
        val span = spec.heatHigh - spec.heatLow
        val steps = 4
        val position = (index % steps).toFloat() / (steps - 1).toFloat()
        return roundToTenth(spec.heatLow + span * position)
    }

    private fun roundToTenth(value: Float): Float = (value * 10f).roundToInt() / 10f
}
