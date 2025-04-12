package com.maka.launcher.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _isPomodoroActive = MutableStateFlow(false)
    val isPomodoroActive: StateFlow<Boolean> = _isPomodoroActive

    fun startPomodoro() {
        _isPomodoroActive.value = true
        // Implement timer logic
    }

    fun stopPomodoro() {
        _isPomodoroActive.value = false
    }
}