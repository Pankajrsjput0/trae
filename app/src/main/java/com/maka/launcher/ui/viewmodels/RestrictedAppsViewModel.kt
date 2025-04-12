package com.maka.launcher.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class RestrictedAppInfo(
    val packageName: String,
    val appName: String,
    val fineAmount: Int,
    val isSessionActive: Boolean = false,
    val remainingTime: Int = 0
)

@HiltViewModel
class RestrictedAppsViewModel @Inject constructor() : ViewModel() {
    private val _restrictedApps = MutableStateFlow<List<RestrictedAppInfo>>(emptyList())
    val restrictedApps: StateFlow<List<RestrictedAppInfo>> = _restrictedApps

    fun updateAppFine(packageName: String, newFine: Int) {
        _restrictedApps.value = _restrictedApps.value.map { app ->
            if (app.packageName == packageName) {
                app.copy(fineAmount = newFine)
            } else {
                app
            }
        }
    }

    fun startAppUsageSession(packageName: String, minutes: Int) {
        // Implement session start logic
        // This should:
        // 1. Calculate fine based on time
        // 2. Check wallet balance
        // 3. Start usage timer
        // 4. Update app state
    }

    fun endAppUsageSession(packageName: String) {
        // Implement session end logic
    }
}