package com.Zihmaho6.Tekloc.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zihmaho6.Tekloc.data.sample.SampleData
import com.Zihmaho6.Tekloc.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val mutable = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = mutable.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                val cards = SampleData.recipes.map { recipe ->
                    val mastery = progress.mastery[recipe.id]
                    RecipeCard(
                        id = recipe.id,
                        nameRes = recipe.nameRes,
                        noteRes = recipe.noteRes,
                        signatureHeat = recipe.signatureHeat,
                        mastery = mastery ?: 0,
                        unlocked = mastery != null
                    )
                }
                mutable.update {
                    it.copy(
                        coins = progress.coins,
                        unlocked = cards.count { card -> card.unlocked },
                        recipes = cards
                    )
                }
            }
        }
    }
}
