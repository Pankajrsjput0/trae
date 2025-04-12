package com.maka.launcher.data.model

data class User(
    val id: String,
    val email: String,
    val coinBalance: Int,
    val settings: UserSettings
)

data class UserSettings(
    val theme: String = "light",
    val fontSize: Int = 16,
    val restrictedApps: List<RestrictedAppSetting> = emptyList()
)

data class RestrictedAppSetting(
    val packageName: String,
    val fineAmount: Int,
    val dailyLimit: Int? = null
)