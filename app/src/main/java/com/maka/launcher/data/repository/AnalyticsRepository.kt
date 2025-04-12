package com.maka.launcher.data.repository

import android.app.usage.UsageStatsManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val supabaseRepository: SupabaseRepository
) {
    private val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    suspend fun getDailyStats(timestamp: Long = System.currentTimeMillis()): DailyStats {
        val startTime = getDayStart(timestamp)
        val endTime = timestamp
        
        val usageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )

        val appUsage = usageStats.associate { stats ->
            stats.packageName to AppUsageStats(
                packageName = stats.packageName,
                totalTimeUsed = stats.totalTimeInForeground,
                lastUsed = stats.lastTimeUsed,
                focusSessionsCompleted = getFocusSessionCount(stats.packageName),
                coinsSpent = getCoinsSpent(stats.packageName)
            )
        }

        return DailyStats(
            date = startTime,
            totalScreenTime = usageStats.sumOf { it.totalTimeInForeground },
            focusSessionsCompleted = getTotalFocusSessions(),
            coinsEarned = getCoinsEarned(),
            coinsSpent = getCoinsSpent(),
            appUsage = appUsage
        )
    }

    private fun getDayStart(timestamp: Long): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private suspend fun getFocusSessionCount(packageName: String): Int {
        return supabaseRepository.getFocusSessionCount(packageName)
    }

    private suspend fun getCoinsSpent(packageName: String? = null): Int {
        return supabaseRepository.getCoinsSpent(packageName)
    }

    private suspend fun getCoinsEarned(): Int {
        return supabaseRepository.getCoinsEarned()
    }

    private suspend fun getTotalFocusSessions(): Int {
        return supabaseRepository.getTotalFocusSessions()
    }
}