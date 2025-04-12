package com.maka

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class FavoriteAppsManager(context: Context) {
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val favoritesKey = "favorite_apps"
    
    fun addFavoriteApp(packageName: String) {
        val currentFavorites = getFavoriteApps().toMutableSet()
        currentFavorites.add(packageName)
        preferences.edit().putStringSet(favoritesKey, currentFavorites).apply()
    }
    
    fun removeFavoriteApp(packageName: String) {
        val currentFavorites = getFavoriteApps().toMutableSet()
        currentFavorites.remove(packageName)
        preferences.edit().putStringSet(favoritesKey, currentFavorites).apply()
    }
    
    fun getFavoriteApps(): Set<String> {
        return preferences.getStringSet(favoritesKey, emptySet()) ?: emptySet()
    }
    
    fun isFavoriteApp(packageName: String): Boolean {
        return getFavoriteApps().contains(packageName)
    }
    
    fun toggleFavoriteApp(packageName: String): Boolean {
        return if (isFavoriteApp(packageName)) {
            removeFavoriteApp(packageName)
            false
        } else {
            addFavoriteApp(packageName)
            true
        }
    }
}