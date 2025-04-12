package com.maka.launcher.data.remote

object NowPaymentsConfig {
    const val API_KEY = "71EC8FX-4YN4PBZ-NPN4SYN-M6WXZAF"
    const val BASE_URL = "https://api.nowpayments.io/v1/"
    
    // Payment statuses
    const val STATUS_WAITING = "waiting"
    const val STATUS_CONFIRMING = "confirming"
    const val STATUS_CONFIRMED = "confirmed"
    const val STATUS_SENDING = "sending"
    const val STATUS_PARTIALLY_PAID = "partially_paid"
    const val STATUS_FINISHED = "finished"
    const val STATUS_FAILED = "failed"
    const val STATUS_REFUNDED = "refunded"
    const val STATUS_EXPIRED = "expired"
}

data class CreatePaymentRequest(
    val priceAmount: Double,
    val priceCurrency: String,
    val paymentCurrency: String,
    val orderId: String? = null,
    val orderDescription: String? = null
)

data class PaymentResponse(
    val paymentId: String,
    val paymentStatus: String,
    val payAddress: String,
    val payAmount: Double,
    val payCurrency: String,
    val priceAmount: Double,
    val priceCurrency: String,
    val orderId: String?,
    val orderDescription: String?,
    val purchaseId: String?
)

data class PaymentStatus(
    val paymentId: String,
    val paymentStatus: String,
    val payAddress: String,
    val payAmount: Double,
    val actuallyPaid: Double,
    val purchaseId: String?
) {
    val isCompleted: Boolean
        get() = paymentStatus == NowPaymentsConfig.STATUS_FINISHED
    
    val isPending: Boolean
        get() = paymentStatus in listOf(
            NowPaymentsConfig.STATUS_WAITING,
            NowPaymentsConfig.STATUS_CONFIRMING,
            NowPaymentsConfig.STATUS_CONFIRMED,
            NowPaymentsConfig.STATUS_SENDING
        )
    
    val isFailed: Boolean
        get() = paymentStatus in listOf(
            NowPaymentsConfig.STATUS_FAILED,
            NowPaymentsConfig.STATUS_REFUNDED,
            NowPaymentsConfig.STATUS_EXPIRED
        )
}