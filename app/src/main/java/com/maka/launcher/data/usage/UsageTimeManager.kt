package com.maka.launcher.data.usage

import com.maka.launcher.data.model.CoinPackages
import com.maka.launcher.data.model.UsageTimeFine

class UsageTimeManager {
    private val defaultFines = CoinPackages.DEFAULT_FINES
    private var currentSession: AppSession? = null

    data class AppSession(
        val packageName: String,
        var startTime: Long,
        var endTime: Long,
        var totalCoins: Int
    )

    fun calculateFine(minutes: Int): Int {
        // First check if there's a predefined fine for this duration
        val predefinedFine = defaultFines.find { it.minutes == minutes }
        if (predefinedFine != null) {
            return predefinedFine.coins
        }

        // If no predefined fine exists, calculate a custom fine
        return CoinPackages.calculateCustomFine(minutes)
    }

    fun getAvailableFines(): List<UsageTimeFine> {
        return defaultFines
    }

    fun startSession(packageName: String, minutes: Int, coins: Int): AppSession {
        val now = System.currentTimeMillis()
        val session = AppSession(
            packageName = packageName,
            startTime = now,
            endTime = now + (minutes * 60 * 1000L),
            totalCoins = coins
        )
        currentSession = session
        return session
    }

    fun extendSession(additionalMinutes: Int): AppSession? {
        val session = currentSession ?: return null
        val additionalCoins = calculateFine(additionalMinutes)
        
        session.endTime += (additionalMinutes * 60 * 1000L)
        session.totalCoins += additionalCoins
        
        return session
    }

    fun getCurrentSession(): AppSession? = currentSession

    fun endSession() {
        currentSession = null
    }

    companion object {
        const val MIN_USAGE_TIME = 5
        const val MAX_USAGE_TIME = 180  // 3 hours max
        const val SESSION_EXTENSION_REMINDER = 30  // Show extension reminder 30 seconds before end
        
        fun isValidUsageTime(minutes: Int): Boolean {
            return minutes in MIN_USAGE_TIME..MAX_USAGE_TIME
        }
    }
}