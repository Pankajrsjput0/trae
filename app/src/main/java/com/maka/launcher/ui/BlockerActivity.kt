package com.maka.launcher.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlockerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageName = intent.getStringExtra("package_name") ?: return

        setContent {
            BlockerScreen(
                packageName = packageName,
                onTimeSelected = { minutes ->
                    // Handle time selection and payment
                },
                onDismiss = { finish() }
            )
        }
    }
}

@Composable
fun BlockerScreen(
    packageName: String,
    onTimeSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "App Access Restricted",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Select usage duration:")
        
        TimeSelectionButtons(
            times = listOf(5, 10, 15, 30),
            onTimeSelected = onTimeSelected
        )
        
        Button(
            onClick = onDismiss,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Cancel")
        }
    }
}

@Composable
fun TimeSelectionButtons(
    times: List<Int>,
    onTimeSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        times.forEach { minutes ->
            Button(
                onClick = { onTimeSelected(minutes) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("$minutes minutes")
            }
        }
    }
}