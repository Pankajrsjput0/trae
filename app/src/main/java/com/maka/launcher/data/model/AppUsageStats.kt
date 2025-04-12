package com.maka.launcher.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AppUsageStats(
    val id: String? = null,
    val packageName: String,
    val appName: String,
    val usageTimeInMillis: Long,
    val lastUsed: Long,
    val userId: String? = null,
    val syncTimestamp: Long = System.currentTimeMillis()
)