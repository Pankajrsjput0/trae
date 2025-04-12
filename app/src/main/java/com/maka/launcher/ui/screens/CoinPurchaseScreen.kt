package com.maka.launcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maka.launcher.ui.viewmodels.CoinPurchaseViewModel

@Composable
fun CoinPurchaseScreen(
    viewModel: CoinPurchaseViewModel = hiltViewModel(),
    onPaymentComplete: () -> Unit
) {
    val paymentState by viewModel.paymentState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Purchase Coins",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        CoinPackages(
            packages = listOf(
                CoinPackage(100, 0.99),
                CoinPackage(500, 4.99),
                CoinPackage(1000, 9.99)
            ),
            onPackageSelected = { viewModel.selectPackage(it) }
        )
        
        when (paymentState) {
            is PaymentState.Created -> {
                PaymentInstructions(paymentState as PaymentState.Created)
            }
            is PaymentState.Processing -> {
                CircularProgressIndicator()
            }
            is PaymentState.Completed -> {
                LaunchedEffect(Unit) {
                    onPaymentComplete()
                }
            }
            is PaymentState.Error -> {
                Text(
                    text = (paymentState as PaymentState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CoinPackages(
    packages: List<CoinPackage>,
    onPackageSelected: (CoinPackage) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        packages.forEach { pkg ->
            Button(
                onClick = { onPackageSelected(pkg) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("${pkg.coins} coins for $${pkg.price}")
            }
        }
    }
}