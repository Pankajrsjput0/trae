package com.maka.launcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maka.launcher.ui.viewmodels.AnalyticsViewModel

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val stats by viewModel.dailyStats.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Today's Statistics",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            StatCard(
                title = "Screen Time",
                value = formatDuration(stats.totalScreenTime)
            )
            
            StatCard(
                title = "Focus Sessions",
                value = "${stats.focusSessionsCompleted} completed"
            )
            
            StatCard(
                title = "Coins",
                value = "Earned: ${stats.coinsEarned} | Spent: ${stats.coinsSpent}"
            )
            
            Text(
                text = "App Usage",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        items(stats.appUsage.size) { index ->
            val (packageName, usage) = stats.appUsage.entries.elementAt(index)
            AppUsageCard(
                appName = getAppName(packageName),
                usage = usage
            )
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}