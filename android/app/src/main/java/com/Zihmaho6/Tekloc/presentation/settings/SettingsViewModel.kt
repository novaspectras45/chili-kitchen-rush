package com.Zihmaho6.Tekloc.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zihmaho6.Tekloc.domain.model.GameSettings
import com.Zihmaho6.Tekloc.domain.repository.ProgressRepository
import com.Zihmaho6.Tekloc.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val mutable = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = mutable.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                mutable.update {
                    it.copy(
                        leftHanded = settings.leftHanded,
                        numericTimer = settings.numericTimer,
                        highContrast = settings.highContrast,
                        reduceMotion = settings.reduceMotion
                    )
                }
            }
        }
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                mutable.update { it.copy(coins = progress.coins) }
            }
        }
    }

    fun setLeftHanded(value: Boolean) = write { it.copy(leftHanded = value) }

    fun setNumericTimer(value: Boolean) = write { it.copy(numericTimer = value) }

    fun setHighContrast(value: Boolean) = write { it.copy(highContrast = value) }

    fun setReduceMotion(value: Boolean) = write { it.copy(reduceMotion = value) }

    fun askReset() = mutable.update { it.copy(confirmReset = true) }

    fun dismissReset() = mutable.update { it.copy(confirmReset = false) }

    fun confirmReset() {
        progressRepository.reset()
        mutable.update { it.copy(confirmReset = false) }
    }

    private fun write(transform: (GameSettings) -> GameSettings) {
        settingsRepository.update(transform(settingsRepository.settings.value))
    }
}
