package com.maka.launcher.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val settings: Flow<AppSettings> = context.dataStore.data.map { preferences ->
        AppSettings(
            theme = preferences[PreferencesKeys.THEME] ?: "light",
            fontSize = preferences[PreferencesKeys.FONT_SIZE] ?: 16,
            notificationsEnabled = preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true,
            dailyScreenTimeLimit = preferences[PreferencesKeys.DAILY_SCREEN_TIME_LIMIT] ?: 0,
            grayscaleEnabled = preferences[PreferencesKeys.GRAYSCALE_ENABLED] ?: false
        )
    }

    suspend fun updateTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme
        }
    }

    suspend fun updateFontSize(size: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SIZE] = size
        }
    }

    suspend fun updateNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun updateDailyScreenTimeLimit(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DAILY_SCREEN_TIME_LIMIT] = minutes
        }
    }

    suspend fun updateGrayscaleEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.GRAYSCALE_ENABLED] = enabled
        }
    }
}