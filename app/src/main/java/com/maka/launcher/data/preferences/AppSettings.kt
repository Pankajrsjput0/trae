package com.maka.launcher.data.preferences

import androidx.datastore.preferences.core.*
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val theme: String = "light",
    val fontSize: Int = 16,
    val notificationsEnabled: Boolean = true,
    val dailyScreenTimeLimit: Int = 0,
    val grayscaleEnabled: Boolean = false
)

object PreferencesKeys {
    val THEME = stringPreferencesKey("theme")
    val FONT_SIZE = intPreferencesKey("font_size")
    val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    val DAILY_SCREEN_TIME_LIMIT = intPreferencesKey("daily_screen_time_limit")
    val GRAYSCALE_ENABLED = booleanPreferencesKey("grayscale_enabled")
}