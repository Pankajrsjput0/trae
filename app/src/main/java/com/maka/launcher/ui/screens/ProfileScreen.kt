package com.maka.launcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maka.launcher.ui.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val themeState by viewModel.themeState.collectAsState()
    val coinBalance by viewModel.coinBalance.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
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
            // User Profile Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Coin Balance",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "$coinBalance coins",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Theme Settings
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Card {
                Column {
                    ListItem(
                        headlineContent = { Text("Theme") },
                        trailingContent = {
                            DropdownMenu(
                                expanded = false,
                                onDismissRequest = { }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Light") },
                                    onClick = { viewModel.setTheme("light") }
                                )
                                DropdownMenuItem(
                                    text = { Text("Dark") },
                                    onClick = { viewModel.setTheme("dark") }
                                )
                                DropdownMenuItem(
                                    text = { Text("Grayscale") },
                                    onClick = { viewModel.setTheme("grayscale") }
                                )
                            }
                        }
                    )

                    ListItem(
                        headlineContent = { Text("Font Size") },
                        trailingContent = {
                            Row {
                                IconButton(onClick = { viewModel.decreaseFontSize() }) {
                                    Icon(Icons.Default.Remove, "Decrease")
                                }
                                IconButton(onClick = { viewModel.increaseFontSize() }) {
                                    Icon(Icons.Default.Add, "Increase")
                                }
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Analytics Section
            Text(
                text = "Analytics",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text("Screen Time Today") },
                        supportingContent = { Text("2h 30m") }
                    )
                    ListItem(
                        headlineContent = { Text("Focus Sessions") },
                        supportingContent = { Text("4 sessions") }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log Out")
            }
        }
    }
}