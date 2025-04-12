package com.maka.launcher.data.payment

import com.maka.launcher.data.model.CoinPackage
import com.maka.launcher.data.remote.NowPaymentsConfig
import com.maka.launcher.ui.viewmodels.PaymentState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaymentManager {
    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: Flow<PaymentState> = _paymentState.asStateFlow()

    suspend fun purchaseCoinPackage(package: CoinPackage, currency: String = "USD") {
        try {
            _paymentState.value = PaymentState.Processing
            
            val amount = if (currency == "INR") package.priceInr else package.priceUsd
            val payment = NowPaymentsConfig.createPayment(amount, currency)
            
            _paymentState.value = PaymentState.Created(
                paymentId = payment.paymentId,
                address = payment.payAddress,
                amount = payment.payAmount,
                currency = currency
            )
        } catch (e: Exception) {
            _paymentState.value = PaymentState.Error(e.message ?: "Payment creation failed")
        }
    }

    suspend fun purchaseCustomCoins(coins: Int, currency: String = "USD") {
        try {
            _paymentState.value = PaymentState.Processing
            
            // Calculate price based on nearest package rate
            val pricePerCoin = when {
                coins <= 100 -> if (currency == "INR") 0.99 else 0.0099  // Starter pack rate
                coins <= 550 -> if (currency == "INR") 0.907 else 0.00907  // Saver pack rate
                coins <= 1200 -> if (currency == "INR") 0.832 else 0.00832  // Pro pack rate
                else -> if (currency == "INR") 0.799 else 0.00799  // Master pack rate
            }
            
            val amount = coins * pricePerCoin
            val payment = NowPaymentsConfig.createPayment(amount, currency)
            
            _paymentState.value = PaymentState.Created(
                paymentId = payment.paymentId,
                address = payment.payAddress,
                amount = payment.payAmount,
                currency = currency
            )
        } catch (e: Exception) {
            _paymentState.value = PaymentState.Error(e.message ?: "Payment creation failed")
        }
    }

    fun checkPaymentStatus(paymentId: String) {
        try {
            val status = NowPaymentsConfig.getPaymentStatus(paymentId)
            
            when {
                status.isCompleted -> _paymentState.value = PaymentState.Completed
                status.isFailed -> _paymentState.value = PaymentState.Error("Payment failed")
                status.isPending -> _paymentState.value = PaymentState.Processing
            }
        } catch (e: Exception) {
            _paymentState.value = PaymentState.Error(e.message ?: "Failed to check payment status")
        }
    }

    fun resetPaymentState() {
        _paymentState.value = PaymentState.Idle
    }
}