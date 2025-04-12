package com.maka.launcher.data.model

data class AppInfo(
    val packageName: String,
    val appName: String,
    val isRestricted: Boolean = false,
    val fineAmount: Int = 10,
    val isFavorite: Boolean = false
)