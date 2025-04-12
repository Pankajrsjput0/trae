package com.maka.launcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maka.launcher.ui.viewmodels.PomodoroViewModel

@Composable
fun PomodoroScreen(
    navController: NavController,
    viewModel: PomodoroViewModel = hiltViewModel()
) {
    val timerState by viewModel.timerState.collectAsState()
    val remainingTime by viewModel.remainingTime.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = formatTime(remainingTime),
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    when (timerState) {
                        TimerState.IDLE -> viewModel.startTimer()
                        TimerState.RUNNING -> viewModel.stopTimer()
                        TimerState.PAUSED -> viewModel.startTimer()
                    }
                },
                modifier = Modifier.size(width = 200.dp, height = 56.dp)
            ) {
                Text(
                    text = when (timerState) {
                        TimerState.IDLE -> "Start Focus"
                        TimerState.RUNNING -> "Stop"
                        TimerState.PAUSED -> "Resume"
                    }
                )
            }
        }
    }
}