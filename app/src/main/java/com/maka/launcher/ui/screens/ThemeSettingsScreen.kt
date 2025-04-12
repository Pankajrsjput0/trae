package com.maka.launcher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maka.launcher.ui.viewmodels.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsScreen(
    navController: NavController,
    viewModel: ThemeViewModel
) {
    val themes by viewModel.availableThemes.collectAsState()
    val currentTheme by viewModel.currentTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Switch(
                checked = currentTheme.isDark,
                onCheckedChange = { viewModel.toggleDarkMode() },
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = if (currentTheme.isDark) "Dark Mode" else "Light Mode",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Color Themes",
                style = MaterialTheme.typography.titleLarge
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(themes) { theme ->
                    ThemePreviewCard(
                        theme = theme,
                        isSelected = theme.name == currentTheme.name,
                        onClick = { viewModel.setTheme(theme) }
                    )
                }
            }
        }
    }
}