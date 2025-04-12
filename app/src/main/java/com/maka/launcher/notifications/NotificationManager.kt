package com.maka.launcher.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.maka.launcher.MainActivity
import com.maka.launcher.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val channels = listOf(
            NotificationChannel(
                CHANNEL_USAGE_ALERTS,
                "Usage Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ),
            NotificationChannel(
                CHANNEL_TIME_LIMITS,
                "Time Limits",
                NotificationManager.IMPORTANCE_DEFAULT
            ),
            NotificationChannel(
                CHANNEL_ACHIEVEMENTS,
                "Achievements",
                NotificationManager.IMPORTANCE_LOW
            )
        )

        channels.forEach { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showTimeWarning(appName: String, minutesLeft: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_TIME_LIMITS)
            .setSmallIcon(R.drawable.ic_timer)
            .setContentTitle("Time Warning")
            .setContentText("$minutesLeft minutes left for $appName")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(createMainActivityIntent())
            .build()

        notificationManager.notify(TIME_WARNING_ID, notification)
    }

    fun showDailyLimitReached(appName: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_USAGE_ALERTS)
            .setSmallIcon(R.drawable.ic_block)
            .setContentTitle("Daily Limit Reached")
            .setContentText("You've reached your daily limit for $appName")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(createMainActivityIntent())
            .build()

        notificationManager.notify(DAILY_LIMIT_ID, notification)
    }

    fun showAchievement(title: String, description: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ACHIEVEMENTS)
            .setSmallIcon(R.drawable.ic_achievement)
            .setContentTitle(title)
            .setContentText(description)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(createMainActivityIntent())
            .build()

        notificationManager.notify(ACHIEVEMENT_ID, notification)
    }

    private fun createMainActivityIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        private const val CHANNEL_USAGE_ALERTS = "usage_alerts"
        private const val CHANNEL_TIME_LIMITS = "time_limits"
        private const val CHANNEL_ACHIEVEMENTS = "achievements"
        
        private const val TIME_WARNING_ID = 1
        private const val DAILY_LIMIT_ID = 2
        private const val ACHIEVEMENT_ID = 3
    }
}