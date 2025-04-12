package com.maka.launcher.data.model

data class CoinPackage(
    val name: String,
    val coins: Int,
    val priceInr: Double,
    val priceUsd: Double,
    val bonusCoins: Int = 0
)

data class UsageTimeFine(
    val minutes: Int,
    val coins: Int
)

object CoinPackages {
    val STARTER = CoinPackage(
        name = "Starter",
        coins = 100,
        priceInr = 99.0,
        priceUsd = 0.99
    )

    val SAVER = CoinPackage(
        name = "Saver",
        coins = 550,
        priceInr = 499.0,
        priceUsd = 4.99,
        bonusCoins = 50
    )

    val PRO = CoinPackage(
        name = "Pro",
        coins = 1200,
        priceInr = 999.0,
        priceUsd = 9.99,
        bonusCoins = 200
    )

    val MASTER = CoinPackage(
        name = "Master",
        coins = 2500,
        priceInr = 1999.0,
        priceUsd = 19.99,
        bonusCoins = 500
    )

    val DEFAULT_FINES = listOf(
        UsageTimeFine(5, 5),
        UsageTimeFine(10, 8),  // Slight discount for longer duration
        UsageTimeFine(15, 12), // More discount for 15 minutes
        UsageTimeFine(30, 20), // Better value for longer sessions
        UsageTimeFine(60, 35), // 1 hour session
        UsageTimeFine(120, 60) // 2 hour session
    )

    fun calculateCustomFine(minutes: Int): Int {
        return when {
            minutes <= 0 -> 0
            minutes <= 5 -> 5  // Minimum fine
            minutes <= 15 -> (minutes * 0.8).toInt()  // 20% discount up to 15 minutes
            minutes <= 30 -> (minutes * 0.7).toInt()  // 30% discount up to 30 minutes
            minutes <= 60 -> (minutes * 0.6).toInt()  // 40% discount up to 1 hour
            else -> (minutes * 0.5).toInt()  // 50% discount for longer durations
        }
    }
}