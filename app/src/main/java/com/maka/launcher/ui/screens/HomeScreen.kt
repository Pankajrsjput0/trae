package com.maka.launcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(navController: NavController) {
    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Time and Date
        Text(
            text = currentTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = currentTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM d")),
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Pomodoro Button
        Button(
            onClick = { /* Start Pomodoro */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Timer, contentDescription = "Timer")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pomodoro Ignite")
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Bottom Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { /* Open Dialer */ }) {
                Icon(Icons.Default.Call, contentDescription = "Phone")
            }
            
            IconButton(onClick = { /* Open Camera */ }) {
                Icon(Icons.Default.Camera, contentDescription = "Camera")
            }
        }
    }
}