package com.maka.launcher.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TimerState {
    IDLE, RUNNING, PAUSED
}

@HiltViewModel
class PomodoroViewModel @Inject constructor() : ViewModel() {
    private val POMODORO_TIME = 25 * 60 // 25 minutes in seconds

    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState

    private val _remainingTime = MutableStateFlow(POMODORO_TIME)
    val remainingTime: StateFlow<Int> = _remainingTime

    private var timerJob: Job? = null

    fun startTimer() {
        if (_timerState.value == TimerState.RUNNING) return

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            _timerState.value = TimerState.RUNNING
            
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value -= 1
            }
            
            if (_remainingTime.value <= 0) {
                _timerState.value = TimerState.IDLE
                _remainingTime.value = POMODORO_TIME
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.PAUSED
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}