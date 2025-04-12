package com.maka.launcher.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maka.launcher.data.repository.AppUsageRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AppUsageSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val appUsageRepository: AppUsageRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Get current app usage stats from local storage
            val currentStats = inputData.keyValueMap["usage_stats"]
            if (currentStats != null) {
                // Sync with Supabase
                // appUsageRepository.syncAppUsageStats(currentStats)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "app_usage_sync_worker"
    }
}