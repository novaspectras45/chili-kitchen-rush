package com.Zihmaho6.Tekloc.data.sample

import com.Zihmaho6.Tekloc.R
import com.Zihmaho6.Tekloc.domain.model.LevelSpec
import com.Zihmaho6.Tekloc.domain.model.Recipe
import com.Zihmaho6.Tekloc.domain.model.ToleranceBand

object SampleData {

    val recipes: List<Recipe> = listOf(
        Recipe(1, R.string.recipe_1, R.string.recipe_1_note, 7),
        Recipe(2, R.string.recipe_2, R.string.recipe_2_note, 9),
        Recipe(3, R.string.recipe_3, R.string.recipe_3_note, 3),
        Recipe(4, R.string.recipe_4, R.string.recipe_4_note, 8),
        Recipe(5, R.string.recipe_5, R.string.recipe_5_note, 4),
        Recipe(6, R.string.recipe_6, R.string.recipe_6_note, 5),
        Recipe(7, R.string.recipe_7, R.string.recipe_7_note, 10),
        Recipe(8, R.string.recipe_8, R.string.recipe_8_note, 6),
        Recipe(9, R.string.recipe_9, R.string.recipe_9_note, 5),
        Recipe(10, R.string.recipe_10, R.string.recipe_10_note, 8),
        Recipe(11, R.string.recipe_11, R.string.recipe_11_note, 6),
        Recipe(12, R.string.recipe_12, R.string.recipe_12_note, 10)
    )

    val levels: List<LevelSpec> = listOf(
        LevelSpec(1, 6, 75, 1.0f, 2f, 6f, ToleranceBand.WIDE),
        LevelSpec(2, 7, 73, 1.0f, 2f, 7f, ToleranceBand.WIDE),
        LevelSpec(3, 7, 71, 0.9f, 1.5f, 7.5f, ToleranceBand.WIDE),
        LevelSpec(4, 8, 69, 0.85f, 1.5f, 8f, ToleranceBand.TIGHT),
        LevelSpec(5, 9, 67, 0.8f, 1f, 8.5f, ToleranceBand.TIGHT),
        LevelSpec(6, 10, 65, 0.75f, 1f, 9f, ToleranceBand.TIGHT),
        LevelSpec(7, 10, 63, 0.7f, 1f, 9.5f, ToleranceBand.TIGHT),
        LevelSpec(8, 11, 61, 0.65f, 1f, 10f, ToleranceBand.TIGHT),
        LevelSpec(9, 12, 59, 0.6f, 1f, 10f, ToleranceBand.RAZOR),
        LevelSpec(10, 13, 56, 0.55f, 1f, 10f, ToleranceBand.RAZOR),
        LevelSpec(11, 13, 54, 0.5f, 1f, 10f, ToleranceBand.RAZOR),
        LevelSpec(12, 14, 52, 0.45f, 1f, 10f, ToleranceBand.RAZOR)
    )

    fun level(index: Int): LevelSpec = levels.getOrElse(index - 1) { levels.first() }

    fun recipe(id: Int): Recipe = recipes.firstOrNull { it.id == id } ?: recipes.first()
}
