package com.maka

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AppBlockerActivity : AppCompatActivity() {
    private lateinit var paymentLayout: LinearLayout
    private lateinit var appNameText: TextView
    private lateinit var adLayout: LinearLayout
    private lateinit var watchAdButton: Button
    private lateinit var timeButtons: Map<Int, Button>
    private var selectedMinutes = 0
    private var requiredCoins = 0
    
    companion object {
        const val EXTRA_PACKAGE_NAME = "package_name"
        const val EXTRA_APP_NAME = "app_name"
        private const val DEFAULT_COIN_RATE = 10 // coins per 10 minutes
        private const val FREE_MINUTES_FROM_AD = 7
        private const val MAX_DAILY_AD_UNLOCKS = 3
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocker)
        
        val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME)
        val appName = intent.getStringExtra(EXTRA_APP_NAME)
        
        if (packageName == null || appName == null) {
            finish()
            return
        }
        
        // Initialize views
        paymentLayout = findViewById(R.id.payment_layout)
        appNameText = findViewById(R.id.app_name_text)
        adLayout = findViewById(R.id.ad_layout)
        watchAdButton = findViewById(R.id.watch_ad_button)
        
        appNameText.text = appName
        
        // Setup time selection buttons
        timeButtons = mapOf(
            5 to findViewById(R.id.button_5min),
            10 to findViewById(R.id.button_10min),
            15 to findViewById(R.id.button_15min),
            30 to findViewById(R.id.button_30min)
        )
        
        setupTimeButtons()
        setupAdButton(packageName)
        checkDailyAdAvailability(packageName)
    }
    
    private fun setupTimeButtons() {
        timeButtons.forEach { (minutes, button) ->
            button.setOnClickListener {
                selectedMinutes = minutes
                requiredCoins = calculateRequiredCoins(minutes)
                showPaymentConfirmation()
            }
        }
    }
    
    private fun setupAdButton(packageName: String) {
        watchAdButton.setOnClickListener {
            // Show rewarded ad
            showRewardedAd { success ->
                if (success) {
                    grantFreeAccess(packageName)
                }
            }
        }
    }
    
    private fun calculateRequiredCoins(minutes: Int): Int {
        return (minutes * DEFAULT_COIN_RATE) / 10
    }
    
    private fun showPaymentConfirmation() {
        // TODO: Show payment confirmation dialog
        // For now, just simulate payment success
        processPayment(true)
    }
    
    private fun processPayment(success: Boolean) {
        if (success) {
            val resultIntent = Intent().apply {
                putExtra("minutes_granted", selectedMinutes)
                putExtra("payment_amount", requiredCoins)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
    
    private fun showRewardedAd(callback: (Boolean) -> Unit) {
        // TODO: Implement actual ad showing logic
        // For now, simulate ad completion
        callback(true)
    }
    
    private fun grantFreeAccess(packageName: String) {
        val resultIntent = Intent().apply {
            putExtra("minutes_granted", FREE_MINUTES_FROM_AD)
            putExtra("free_access", true)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
    
    private fun checkDailyAdAvailability(packageName: String) {
        // TODO: Implement daily ad limit checking
        // For now, always show ad option
        adLayout.visibility = View.VISIBLE
        paymentLayout.visibility = View.VISIBLE
    }
}