package com.maka.launcher.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _themeState = MutableStateFlow("light")
    val themeState: StateFlow<String> = _themeState

    private val _coinBalance = MutableStateFlow(100)
    val coinBalance: StateFlow<Int> = _coinBalance

    fun setTheme(theme: String) {
        _themeState.value = theme
    }

    fun increaseFontSize() {
        // Implement font size increase logic
    }

    fun decreaseFontSize() {
        // Implement font size decrease logic
    }

    fun logout() {
        // Implement logout logic
    }
}