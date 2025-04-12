package com.maka.launcher.data.repository

import com.maka.launcher.data.SupabaseClient
import com.maka.launcher.data.model.AppUsageStats
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUsageRepository @Inject constructor() {
    private val supabase = SupabaseClient.client

    suspend fun syncAppUsageStats(stats: AppUsageStats) {
        try {
            supabase.postgrest["app_usage_stats"].insert(stats)
        } catch (e: Exception) {
            // Handle error (e.g., store locally for later sync)
        }
    }

    fun getAppUsageStats(userId: String): Flow<List<AppUsageStats>> = flow {
        try {
            val stats = supabase.postgrest["app_usage_stats"]
                .select { eq("user_id", userId) }
                .decodeList<AppUsageStats>()
            emit(stats)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    suspend fun deleteAppUsageStats(statsId: String) {
        try {
            supabase.postgrest["app_usage_stats"]
                .delete { eq("id", statsId) }
        } catch (e: Exception) {
            // Handle error
        }
    }
}