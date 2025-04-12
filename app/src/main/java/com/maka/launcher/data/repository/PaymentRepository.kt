package com.maka.launcher.data.repository

import com.maka.launcher.data.remote.CreatePaymentRequest
import com.maka.launcher.data.remote.NowPaymentsConfig
import com.maka.launcher.data.remote.PaymentResponse
import com.maka.launcher.data.remote.PaymentStatus
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor() {
    private val apiClient: NowPaymentsApiClient
    
    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-api-key", NowPaymentsConfig.API_KEY)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()
            
        val retrofit = Retrofit.Builder()
            .baseUrl(NowPaymentsConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            
        apiClient = retrofit.create(NowPaymentsApiClient::class.java)
    }

    suspend fun createPayment(amount: Double, currency: String = "USD"): PaymentResponse {
        return apiClient.createPayment(
            CreatePaymentRequest(
                priceAmount = amount,
                priceCurrency = currency,
                paymentCurrency = "BTC",
                orderId = UUID.randomUUID().toString(),
                orderDescription = "Coin purchase"
            )
        )
    }

    suspend fun checkPaymentStatus(paymentId: String): PaymentStatus {
        return apiClient.getPaymentStatus(paymentId)
    }
    
    suspend fun getMinimumPaymentAmount(currency: String = "BTC"): Double {
        return apiClient.getMinimumPaymentAmount(currency).minAmount
    }
}

interface NowPaymentsApiClient {
    @POST("payment")
    suspend fun createPayment(@Body request: CreatePaymentRequest): PaymentResponse

    @GET("payment/{paymentId}/")
    suspend fun getPaymentStatus(@Path("paymentId") paymentId: String): PaymentStatus
    
    @GET("min-amount/{currency}")
    suspend fun getMinimumPaymentAmount(@Path("currency") currency: String): MinAmountResponse
}

data class MinAmountResponse(
    val minAmount: Double
}