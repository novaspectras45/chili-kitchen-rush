package com.Zihmaho6.Tekloc.presentation.app

import androidx.lifecycle.ViewModel
import com.Zihmaho6.Tekloc.core.navigation.Screen
import com.Zihmaho6.Tekloc.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val backStack = ArrayList<Screen>()
    private val attempts = HashMap<Int, Int>()

    private val current = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = current.asStateFlow()

    fun openMenu() {
        backStack.clear()
        current.value = Screen.Menu
    }

    fun open(target: Screen) {
        if (current.value !is Screen.Splash) backStack.add(current.value)
        current.value = target
    }

    fun launchFromMenu(level: Int) {
        backStack.clear()
        backStack.add(Screen.Menu)
        current.value = gameScreen(level)
    }

    fun launchFromRoute(level: Int) {
        current.value = gameScreen(level)
    }

    fun showResult(target: Screen) {
        current.value = target
    }

    fun back() {
        if (backStack.isEmpty()) {
            current.value = Screen.Menu
            return
        }
        val last = backStack[backStack.size - 1]
        backStack.removeAt(backStack.size - 1)
        current.value = last
    }

    private fun gameScreen(level: Int): Screen.Game {
        progressRepository.rememberLevel(level)
        val attempt = (attempts[level] ?: 0) + 1
        attempts[level] = attempt
        return Screen.Game(level, attempt)
    }
}
