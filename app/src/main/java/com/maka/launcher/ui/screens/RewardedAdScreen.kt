package com.maka.launcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maka.launcher.ui.viewmodels.RewardedAdViewModel

@Composable
fun RewardedAdScreen(
    onAdComplete: (Int) -> Unit,
    viewModel: RewardedAdViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val adState by viewModel.adState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Watch Ad for Free Minutes",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        when (adState) {
            is AdState.NotLoaded -> {
                Button(onClick = { viewModel.loadAd() }) {
                    Text("Load Ad")
                }
            }
            is AdState.Loading -> {
                CircularProgressIndicator()
            }
            is AdState.Loaded -> {
                Button(
                    onClick = { 
                        viewModel.showAd(context as android.app.Activity) { minutes ->
                            onAdComplete(minutes)
                        }
                    }
                ) {
                    Text("Watch Ad")
                }
            }
            is AdState.Error -> {
                Text(
                    text = (adState as AdState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                Button(
                    onClick = { viewModel.loadAd() },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Try Again")
                }
            }
        }
    }
}