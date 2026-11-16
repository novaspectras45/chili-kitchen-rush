package com.Zihmaho6.Tekloc.domain.model

data class OrderTicket(
    val id: Int,
    val recipeId: Int,
    val targetHeat: Float,
    val rush: Boolean,
    val rushRemaining: Float
)
