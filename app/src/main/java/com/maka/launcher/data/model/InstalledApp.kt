package com.maka.launcher.data.model

data class InstalledApp(
    val packageName: String,
    val appName: String,
    val icon: android.graphics.drawable.Drawable,
    val isSystemApp: Boolean,
    val installDate: Long
) {
    fun matchesSearch(query: String): Boolean {
        return appName.contains(query, ignoreCase = true) ||
               packageName.contains(query, ignoreCase = true)
    }

    fun isRecentlyInstalled(timeThreshold: Long = DEFAULT_RECENT_THRESHOLD): Boolean {
        return System.currentTimeMillis() - installDate <= timeThreshold
    }

    companion object {
        const val DEFAULT_RECENT_THRESHOLD = 24 * 60 * 60 * 1000L // 24 hours in milliseconds
    }
}