package com.maka.launcher.ui.viewmodels

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maka.launcher.data.model.InstalledApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AppSortOrder {
    ALPHABETICAL,
    INSTALL_DATE,
    LAST_USED
}

@HiltViewModel
class AppDrawerViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    private val _apps = MutableStateFlow<List<InstalledApp>>(emptyList())
    val apps: StateFlow<List<InstalledApp>> = _apps

    private val _recentlyInstalledApps = MutableStateFlow<List<InstalledApp>>(emptyList())
    val recentlyInstalledApps: StateFlow<List<InstalledApp>> = _recentlyInstalledApps

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _currentSortOrder = MutableStateFlow(AppSortOrder.ALPHABETICAL)
    val currentSortOrder: StateFlow<AppSortOrder> = _currentSortOrder

    init {
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        viewModelScope.launch {
            val pm = getApplication<Application>().packageManager
            val installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
                .map { appInfo ->
                    InstalledApp(
                        packageName = appInfo.packageName,
                        appName = pm.getApplicationLabel(appInfo).toString(),
                        icon = pm.getApplicationIcon(appInfo.packageName),
                        isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0,
                        installDate = pm.getPackageInfo(appInfo.packageName, 0).firstInstallTime
                    )
                }

            _recentlyInstalledApps.value = installedApps.filter { it.isRecentlyInstalled() }
            updateAppsList(installedApps)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        updateAppsList(_apps.value)
    }

    fun setSortOrder(order: AppSortOrder) {
        _currentSortOrder.value = order
        updateAppsList(_apps.value)
    }

    private fun updateAppsList(apps: List<InstalledApp>) {
        val filteredApps = if (_searchQuery.value.isNotEmpty()) {
            apps.filter { it.matchesSearch(_searchQuery.value) }
        } else {
            apps
        }

        val sortedApps = when (_currentSortOrder.value) {
            AppSortOrder.ALPHABETICAL -> filteredApps.sortedBy { it.appName }
            AppSortOrder.INSTALL_DATE -> filteredApps.sortedByDescending { it.installDate }
            AppSortOrder.LAST_USED -> filteredApps.sortedBy { it.appName } // TODO: Implement last used tracking
        }

        _apps.update { sortedApps }
    }
}