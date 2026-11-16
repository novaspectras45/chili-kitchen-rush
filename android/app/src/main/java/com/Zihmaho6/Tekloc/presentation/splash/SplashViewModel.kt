package com.Zihmaho6.Tekloc.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zihmaho6.Tekloc.core.config.GameConfig
import com.Zihmaho6.Tekloc.domain.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SplashUiState(
    val finished: Boolean = false,
    val reduceMotion: Boolean = false
)

class SplashViewModel(
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val mutable = MutableStateFlow(
        SplashUiState(reduceMotion = settingsRepository.settings.value.reduceMotion)
    )
    val state: StateFlow<SplashUiState> = mutable.asStateFlow()

    init {
        viewModelScope.launch {
            delay(GameConfig.LOADER_DURATION_MS)
            mutable.update { it.copy(finished = true) }
        }
    }
}
