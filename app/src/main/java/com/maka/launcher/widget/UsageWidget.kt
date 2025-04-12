package com.maka.launcher.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.maka.launcher.R
import com.maka.launcher.data.repository.SupabaseRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UsageWidget : AppWidgetProvider() {
    @Inject
    lateinit var supabaseRepository: SupabaseRepository

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val mostUsedApps = supabaseRepository.getMostUsedApps(
                userId = getCurrentUserId(),
                limit = 3
            )
            
            appWidgetIds.forEach { appWidgetId ->
                updateAppWidget(context, appWidgetManager, appWidgetId, mostUsedApps)
            }
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        apps: List<String>
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_usage)
        
        apps.forEachIndexed { index, packageName ->
            val appName = getAppName(context, packageName)
            val timeLeft = calculateTimeLeft(packageName)
            
            when (index) {
                0 -> {
                    views.setTextViewText(R.id.app1_name, appName)
                    views.setTextViewText(R.id.app1_time, formatTime(timeLeft))
                }
                1 -> {
                    views.setTextViewText(R.id.app2_name, appName)
                    views.setTextViewText(R.id.app2_time, formatTime(timeLeft))
                }
                2 -> {
                    views.setTextViewText(R.id.app3_name, appName)
                    views.setTextViewText(R.id.app3_time, formatTime(timeLeft))
                }
            }
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun formatTime(minutes: Int): String {
        return when {
            minutes < 0 -> "Blocked"
            minutes < 60 -> "$minutes min"
            else -> "${minutes / 60}h ${minutes % 60}m"
        }
    }
}