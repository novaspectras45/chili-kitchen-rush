package com.Zihmaho6.Tekloc.domain.repository

import com.Zihmaho6.Tekloc.domain.model.KitchenProgress
import com.Zihmaho6.Tekloc.domain.model.ShiftResult
import com.Zihmaho6.Tekloc.domain.model.Station
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {

    val progress: StateFlow<KitchenProgress>

    fun applyShift(result: ShiftResult)

    fun markRecipe(recipeId: Int, perfect: Boolean)

    fun rememberLevel(level: Int)

    fun upgrade(station: Station): Boolean

    fun reset()
}
