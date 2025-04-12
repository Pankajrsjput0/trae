package com.maka.launcher.shortcuts

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import com.maka.launcher.R
import com.maka.launcher.ui.screens.PomodoroActivity
import com.maka.launcher.ui.screens.QuickBlockActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppShortcutManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val shortcutManager = context.getSystemService(ShortcutManager::class.java)

    fun updateShortcuts(frequentApps: List<String>) {
        val shortcuts = mutableListOf<ShortcutInfo>()

        // Quick Block Shortcut
        shortcuts.add(
            ShortcutInfo.Builder(context, "quick_block")
                .setShortLabel("Quick Block")
                .setLongLabel("Block Distracting Apps")
                .setIcon(Icon.createWithResource(context, R.drawable.ic_block))
                .setIntent(
                    Intent(context, QuickBlockActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                )
                .build()
        )

        // Pomodoro Timer Shortcut
        shortcuts.add(
            ShortcutInfo.Builder(context, "pomodoro")
                .setShortLabel("Pomodoro")
                .setLongLabel("Start Pomodoro Timer")
                .setIcon(Icon.createWithResource(context, R.drawable.ic_timer))
                .setIntent(
                    Intent(context, PomodoroActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                )
                .build()
        )

        // Add dynamic shortcuts for most used apps
        frequentApps.take(2).forEach { packageName ->
            shortcuts.add(
                ShortcutInfo.Builder(context, "block_$packageName")
                    .setShortLabel("Block ${getAppName(packageName)}")
                    .setLongLabel("Block ${getAppName(packageName)}")
                    .setIcon(Icon.createWithResource(context, R.drawable.ic_block_app))
                    .setIntent(
                        Intent(context, QuickBlockActivity::class.java)
                            .setAction(Intent.ACTION_VIEW)
                            .putExtra("package_name", packageName)
                    )
                    .build()
            )
        }

        shortcutManager.dynamicShortcuts = shortcuts
    }

    private fun getAppName(packageName: String): String {
        val packageManager = context.packageManager
        return try {
            packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(packageName, 0)
            ).toString()
        } catch (e: Exception) {
            packageName
        }
    }
}