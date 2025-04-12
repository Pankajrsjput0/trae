package com.maka

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class AppDrawerActivity : AppCompatActivity() {
    private lateinit var searchBar: EditText
    private lateinit var recentlyInstalledSection: View
    private lateinit var recentlyInstalledList: RecyclerView
    private lateinit var allAppsList: RecyclerView
    private lateinit var settingsButton: ImageView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_drawer)
        
        // Initialize views
        searchBar = findViewById(R.id.search_bar)
        recentlyInstalledSection = findViewById(R.id.recently_installed_section)
        recentlyInstalledList = findViewById(R.id.recently_installed_list)
        allAppsList = findViewById(R.id.all_apps_list)
        settingsButton = findViewById(R.id.settings_button)
        
        // Setup RecyclerViews
        recentlyInstalledList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        allAppsList.layoutManager = LinearLayoutManager(this)
        
        // Load apps
        loadApps()
        
        // Setup settings button
        settingsButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
    
    private fun loadApps() {
        val packageManager = packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        
        // Filter system apps
        val userApps = installedApps.filter { app ->
            packageManager.getLaunchIntentForPackage(app.packageName) != null
        }
        
        // Sort apps alphabetically
        val sortedApps = userApps.sortedBy { app ->
            packageManager.getApplicationLabel(app).toString().lowercase()
        }
        
        // Get recently installed apps (last 24 hours)
        val currentTime = System.currentTimeMillis()
        val recentApps = userApps.filter { app ->
            try {
                val installTime = packageManager.getPackageInfo(app.packageName, 0).firstInstallTime
                (currentTime - installTime) <= 24 * 60 * 60 * 1000 // 24 hours in milliseconds
            } catch (e: Exception) {
                false
            }
        }
        
        // Update UI
        updateRecentlyInstalledSection(recentApps)
        updateAllAppsList(sortedApps)
    }
    
    private fun updateRecentlyInstalledSection(recentApps: List<android.content.pm.ApplicationInfo>) {
        if (recentApps.isEmpty()) {
            recentlyInstalledSection.visibility = View.GONE
        } else {
            recentlyInstalledSection.visibility = View.VISIBLE
            // TODO: Set adapter for recently installed apps
        }
    }
    
    private fun updateAllAppsList(apps: List<android.content.pm.ApplicationInfo>) {
        // TODO: Set adapter for all apps
    }
    
    private fun showAppOptions(packageName: String, appName: String) {
        // TODO: Show popup menu with options:
        // - Add to Favorites
        // - Add to Restricted Apps
        // - Rename App
        // - App Info
        // - Uninstall App
    }
    
    private fun addToFavorites(packageName: String) {
        // TODO: Implement adding app to favorites
    }
    
    private fun addToRestrictedApps(packageName: String) {
        // TODO: Implement adding app to restricted list
    }
    
    private fun renameApp(packageName: String) {
        // TODO: Implement app renaming (store in preferences)
    }
    
    private fun showAppInfo(packageName: String) {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = android.net.Uri.parse("package:$packageName")
        startActivity(intent)
    }
    
    private fun uninstallApp(packageName: String) {
        val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE)
        intent.data = android.net.Uri.parse("package:$packageName")
        startActivity(intent)
    }
}