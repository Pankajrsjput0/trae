package com.maka

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoriteAppsActivity : AppCompatActivity() {
    private lateinit var favoriteAppsManager: FavoriteAppsManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoriteAppsAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_apps)
        
        favoriteAppsManager = FavoriteAppsManager(this)
        recyclerView = findViewById(R.id.favorite_apps_recycler)
        
        setupRecyclerView()
        loadFavoriteApps()
    }
    
    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        adapter = FavoriteAppsAdapter { packageName, appName ->
            val intent = Intent(this, AppBlockerActivity::class.java).apply {
                putExtra(AppBlockerActivity.EXTRA_PACKAGE_NAME, packageName)
                putExtra(AppBlockerActivity.EXTRA_APP_NAME, appName)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
    
    private fun loadFavoriteApps() {
        val packageManager = packageManager
        val favoriteApps = favoriteAppsManager.getFavoriteApps()
            .mapNotNull { packageName ->
                try {
                    val appInfo = packageManager.getApplicationInfo(packageName, 0)
                    AppInfo(
                        packageName = packageName,
                        appName = packageManager.getApplicationLabel(appInfo).toString(),
                        icon = packageManager.getApplicationIcon(packageName)
                    )
                } catch (e: PackageManager.NameNotFoundException) {
                    null
                }
            }
        adapter.submitList(favoriteApps)
    }
    
    private class FavoriteAppsAdapter(
        private val onAppClick: (packageName: String, appName: String) -> Unit
    ) : RecyclerView.Adapter<FavoriteAppsAdapter.ViewHolder>() {
        private var apps = listOf<AppInfo>()
        
        fun submitList(newApps: List<AppInfo>) {
            apps = newApps
            notifyDataSetChanged()
        }
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite_app, parent, false)
            return ViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val app = apps[position]
            holder.bind(app)
            holder.itemView.setOnClickListener {
                onAppClick(app.packageName, app.appName)
            }
        }
        
        override fun getItemCount() = apps.size
        
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val iconView: ImageView = itemView.findViewById(R.id.app_icon)
            private val nameView: TextView = itemView.findViewById(R.id.app_name)
            
            fun bind(app: AppInfo) {
                iconView.setImageDrawable(app.icon)
                nameView.text = app.appName
            }
        }
    }
    
    private data class AppInfo(
        val packageName: String,
        val appName: String,
        val icon: android.graphics.drawable.Drawable
    )
}