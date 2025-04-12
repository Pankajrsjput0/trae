package com.maka.launcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maka.launcher.ui.viewmodels.RestrictedAppsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestrictedAppsScreen(
    navController: NavController,
    viewModel: RestrictedAppsViewModel = hiltViewModel()
) {
    val restrictedApps by viewModel.restrictedApps.collectAsState()
    var showTimeDialog by remember { mutableStateOf(false) }
    var selectedApp by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Restricted Apps") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(restrictedApps) { app ->
                RestrictedAppItem(
                    app = app,
                    onTimeSelect = {
                        selectedApp = app.packageName
                        showTimeDialog = true
                    },
                    onFineChange = { newFine ->
                        viewModel.updateAppFine(app.packageName, newFine)
                    }
                )
            }
        }

        if (showTimeDialog) {
            TimeSelectionDialog(
                onDismiss = { showTimeDialog = false },
                onTimeSelected = { minutes ->
                    selectedApp?.let { packageName ->
                        viewModel.startAppUsageSession(packageName, minutes)
                    }
                    showTimeDialog = false
                }
            )
        }
    }
}

@Composable
fun RestrictedAppItem(
    app: RestrictedAppInfo,
    onTimeSelect: () -> Unit,
    onFineChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = app.appName,
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Fine: ${app.fineAmount} coins/10min")
                Button(onClick = onTimeSelect) {
                    Text("Use App")
                }
            }
        }
    }
}

@Composable
fun TimeSelectionDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (Int) -> Unit
) {
    val timeOptions = listOf(5, 10, 15, 30)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Usage Time") },
        text = {
            Column {
                timeOptions.forEach { minutes ->
                    TextButton(
                        onClick = { onTimeSelected(minutes) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("$minutes minutes")
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}