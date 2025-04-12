package com.maka.launcher.data.repository

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseRepository @Inject constructor() {
    private val client = createSupabaseClient(
        supabaseUrl = SupabaseConfig.SUPABASE_URL,
        supabaseKey = SupabaseConfig.SUPABASE_ANON_KEY
    ) {
        install(GoTrue)
        install(Postgrest)
    }

    suspend fun getUserSettings(userId: String): UserSettings {
        return client.postgrest["user_settings"]
            .select { eq("user_id", userId) }
            .decodeSingle()
    }

    suspend fun updateUserSettings(userId: String, settings: UserSettings) {
        client.postgrest["user_settings"]
            .upsert(settings) {
                eq("user_id", userId)
            }
    }

    suspend fun updateCoinBalance(userId: String, newBalance: Int) {
        client.postgrest["users"]
            .update({ set("coin_balance", newBalance) }) {
                eq("id", userId)
            }
    }

    suspend fun saveAppUsageStats(userId: String, stats: AppUsageStats) {
        client.postgrest["app_usage_stats"]
            .insert(stats)
    }
}