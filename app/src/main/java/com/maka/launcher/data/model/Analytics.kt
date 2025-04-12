package com.maka.launcher.data.model

data class AppUsageStats(
    val packageName: String,
    val totalTimeUsed: Long,
    val lastUsed: Long,
    val focusSessionsCompleted: Int,
    val coinsSpent: Int
)

data class DailyStats(
    val date: Long,
    val totalScreenTime: Long,
    val focusSessionsCompleted: Int,
    val coinsEarned: Int,
    val coinsSpent: Int,
    val appUsage: Map<String, AppUsageStats>
)