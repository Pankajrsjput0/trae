package com.maka.launcher.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.maka.launcher.data.repository.SettingsRepository
import com.maka.launcher.ui.theme.DefaultThemes
import com.maka.launcher.ui.theme.ThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _currentTheme = MutableStateFlow(DefaultThemes[0])
    val currentTheme: StateFlow<ThemeConfig> = _currentTheme

    val availableThemes = flowOf(DefaultThemes)

    init {
        // Load saved theme from settings
        settingsRepository.settings.map { settings ->
            DefaultThemes.find { it.name == settings.theme } ?: DefaultThemes[0]
        }.onEach { theme ->
            _currentTheme.value = theme
        }
    }

    fun setTheme(theme: ThemeConfig) {
        _currentTheme.value = theme
        // Save theme preference
        settingsRepository.updateTheme(theme.name)
    }

    fun toggleDarkMode() {
        val currentTheme = _currentTheme.value
        val darkTheme = DefaultThemes.find { 
            it.name == currentTheme.name && it.isDark != currentTheme.isDark 
        } ?: DefaultThemes.find { it.isDark != currentTheme.isDark } 
        ?: return

        setTheme(darkTheme)
    }
}