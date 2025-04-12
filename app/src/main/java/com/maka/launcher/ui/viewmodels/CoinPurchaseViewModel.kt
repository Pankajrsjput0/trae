package com.maka.launcher.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maka.launcher.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinPurchaseViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {
    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: StateFlow<PaymentState> = _paymentState
    
    private var statusCheckJob: Job? = null
    private var minimumAmount: Double = 0.0
    
    init {
        viewModelScope.launch {
            try {
                minimumAmount = paymentRepository.getMinimumPaymentAmount()
            } catch (e: Exception) {
                _paymentState.value = PaymentState.Error("Failed to fetch minimum payment amount")
            }
        }
    }

    fun selectPackage(coinPackage: CoinPackage) {
        if (coinPackage.price < minimumAmount) {
            _paymentState.value = PaymentState.Error("Amount is below minimum payment threshold")
            return
        }
        
        viewModelScope.launch {
            try {
                _paymentState.value = PaymentState.Processing
                val payment = paymentRepository.createPayment(coinPackage.price)
                _paymentState.value = PaymentState.Created(
                    paymentId = payment.paymentId,
                    address = payment.payAddress,
                    amount = payment.payAmount,
                    currency = payment.payCurrency
                )
                startPaymentStatusCheck(payment.paymentId)
            } catch (e: Exception) {
                _paymentState.value = PaymentState.Error(e.message ?: "Payment creation failed")
            }
        }
    }

    private fun startPaymentStatusCheck(paymentId: String) {
        statusCheckJob?.cancel()
        statusCheckJob = viewModelScope.launch {
            while (true) {
                try {
                    val status = paymentRepository.checkPaymentStatus(paymentId)
                    when {
                        status.isCompleted -> {
                            _paymentState.value = PaymentState.Completed
                            break
                        }
                        status.isFailed -> {
                            _paymentState.value = PaymentState.Error("Payment failed or expired")
                            break
                        }
                        status.isPending -> {
                            _paymentState.value = PaymentState.Processing
                        }
                    }
                    kotlinx.coroutines.delay(10000) // Check every 10 seconds
                } catch (e: Exception) {
                    _paymentState.value = PaymentState.Error("Failed to check payment status")
                    break
                }
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        statusCheckJob?.cancel()
    }
}