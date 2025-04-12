package com.maka.launcher.services

import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Process
import com.maka.launcher.data.repository.SupabaseRepository
import com.maka.launcher.ui.BlockerActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class AppMonitoringService : Service() {
    @Inject
    lateinit var shortcutManager: AppShortcutManager
    @Inject
    lateinit var notificationManager: AppNotificationManager
    @Inject
    lateinit var supabaseRepository: SupabaseRepository
    @Inject
    lateinit var backupManager: BackupManager

    private val usageCache = mutableMapOf<String, Long>()
    private val lastUpdateTime = mutableMapOf<String, Long>()
    private val pendingSync = mutableSetOf<String>()
    private var lastSyncTime = 0L
    private var lastShortcutUpdate = 0L

    private val syncJob = Job()
    private val syncScope = CoroutineScope(Dispatchers.IO + syncJob)

    override fun onCreate() {
        super.onCreate()
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        startMonitoring()
        startPeriodicSync()
    }

    private fun startPeriodicSync() {
        syncScope.launch {
            while (isActive) {
                syncPendingData()
                delay(300000) // Sync every 5 minutes
            }
        }
    }

    private suspend fun syncPendingData() {
        if (pendingSync.isEmpty()) return

        try {
            val currentTime = System.currentTimeMillis()
            val syncData = pendingSync.map { packageName ->
                AppUsageStats(
                    packageName = packageName,
                    usageTime = getAppUsageTime(packageName),
                    timestamp = currentTime
                )
            }

            supabaseRepository.syncAppUsageStats(getCurrentUserId(), syncData)
            backupManager.backupUserData(getCurrentUserId())
            
            pendingSync.clear()
            lastSyncTime = currentTime
        } catch (e: Exception) {
            notificationManager.showSyncError("Failed to sync app usage data")
        }
    }

    private suspend fun handleAppUsage(packageName: String) {
        val currentTime = System.currentTimeMillis()
        lastUpdateTime[packageName] = currentTime

        if (shouldUpdateStats(packageName)) {
            val usageTime = getAppUsageTime(packageName)
            supabaseRepository.saveAppUsageStats(
                getCurrentUserId(),
                AppUsageStats(
                    packageName = packageName,
                    usageTime = usageTime,
                    timestamp = currentTime
                )
            )
            usageCache[packageName] = currentTime
            pendingSync.add(packageName)
            
            // Update shortcuts every hour
            if (currentTime - lastShortcutUpdate >= 3600000) {
                updateShortcuts()
                lastShortcutUpdate = currentTime
            }
        }
    }

    private suspend fun updateShortcuts() {
        val frequentApps = supabaseRepository.getMostUsedApps(
            userId = getCurrentUserId(),
            limit = 2
        )
        shortcutManager.updateShortcuts(frequentApps)
    }

    override fun onDestroy() {
        super.onDestroy()
        syncScope.launch {
            syncPendingData() // Final sync before service destruction
        }
        syncJob.cancel()
        serviceJob.cancel()
    }

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)
    private lateinit var usageStatsManager: UsageStatsManager
    private lateinit var activityManager: ActivityManager

    override fun onCreate() {
        super.onCreate()
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        startMonitoring()
    }

    private fun startMonitoring() {
        serviceScope.launch {
            while (isActive) {
                checkCurrentApp()
                delay(500) // Check more frequently
            }
        }
    }

    private suspend fun checkCurrentApp() {
        val currentApp = getCurrentForegroundApp()
        val settings = supabaseRepository.getUserSettings(getCurrentUserId())
        val restrictedApp = settings.restrictedApps.find { it.packageName == currentApp }
        
        restrictedApp?.let {
            val timeLeft = calculateTimeLeft(currentApp)
            
            when {
                timeLeft <= 5 -> {
                    notificationManager.showTimeWarning(
                        getAppName(currentApp),
                        timeLeft
                    )
                }
                timeLeft <= 0 -> {
                    notificationManager.showDailyLimitReached(
                        getAppName(currentApp)
                    )
                    blockApp(currentApp)
                }
            }
            
            handleAppUsage(currentApp)
        }
    }

    private suspend fun calculateTimeLeft(packageName: String): Int {
        val settings = supabaseRepository.getUserSettings(getCurrentUserId())
        val restrictedApp = settings.restrictedApps.find { it.packageName == packageName }
            ?: return Int.MAX_VALUE

        val dailyLimit = restrictedApp.dailyLimit ?: return Int.MAX_VALUE
        val usedTime = getAppUsageTime(packageName)
        
        return (dailyLimit * 60 - usedTime / 1000).toInt()
    }

    private fun getAppUsageTime(packageName: String): Long {
        val currentTime = System.currentTimeMillis()
        val startOfDay = getStartOfDay()
        
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startOfDay,
            currentTime
        )

        val appStats = stats.find { it.packageName == packageName }
        val baseTime = appStats?.totalTimeInForeground ?: 0L

        // Add current session time if app is still in use
        return if (packageName == getCurrentForegroundApp()) {
            val lastUpdate = lastUpdateTime[packageName] ?: currentTime
            val additionalTime = currentTime - lastUpdate
            baseTime + additionalTime
        } else {
            baseTime
        }
    }

    private fun getStartOfDay(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    private fun shouldUpdateStats(packageName: String): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastUpdate = usageCache[packageName] ?: 0L
        return currentTime - lastUpdate >= 60000 // Update every minute
    }

    private fun getCurrentForegroundApp(): String {
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 1000
        
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        )
        
        return stats.maxByOrNull { it.lastTimeUsed }?.packageName ?: ""
    }

    private suspend fun isAppAllowedToRun(packageName: String): Boolean {
        val session = supabaseRepository.getActiveSession(packageName)
        return session?.isValid == true
    }

    private fun blockApp(packageName: String) {
        val intent = Intent(this, BlockerActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("package_name", packageName)
        }
        startActivity(intent)
    }
}