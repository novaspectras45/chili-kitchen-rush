package com.Zihmaho6.Tekloc.presentation.collection

data class RecipeCard(
    val id: Int,
    val nameRes: Int,
    val noteRes: Int,
    val signatureHeat: Int,
    val mastery: Int,
    val unlocked: Boolean
)

data class CollectionUiState(
    val coins: Int = 0,
    val unlocked: Int = 0,
    val recipes: List<RecipeCard> = emptyList()
)
