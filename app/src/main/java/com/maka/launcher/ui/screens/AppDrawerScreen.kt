package com.maka.launcher.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maka.launcher.ui.viewmodels.AppDrawerViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppDrawerScreen(
    navController: NavController,
    viewModel: AppDrawerViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val apps by viewModel.apps.collectAsState()
    val recentlyInstalledApps by viewModel.recentlyInstalledApps.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search apps") },
            leadingIcon = { Icon(Icons.Default.Search, "Search") }
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            // Recently Installed Section
            if (recentlyInstalledApps.isNotEmpty()) {
                stickyHeader {
                    Text(
                        "Recently Installed",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(recentlyInstalledApps) { app ->
                    AppItem(app, viewModel)
                }
            }

            // All Apps Section
            stickyHeader {
                Text(
                    "All Apps",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(apps) { app ->
                AppItem(app, viewModel)
            }
        }

        // Settings Button
        IconButton(
            onClick = { navController.navigate("profile") },
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.End)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Settings, "Settings")
        }
    }
}