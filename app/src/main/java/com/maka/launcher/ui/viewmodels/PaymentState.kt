package com.maka.launcher.ui.viewmodels

sealed class PaymentState {
    object Idle : PaymentState()
    object Processing : PaymentState()
    object Completed : PaymentState()
    
    data class Created(
        val paymentId: String,
        val address: String,
        val amount: Double,
        val currency: String
    ) : PaymentState()
    
    data class Error(val message: String) : PaymentState()
}