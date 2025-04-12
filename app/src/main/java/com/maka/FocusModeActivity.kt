package com.maka

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FocusModeActivity : AppCompatActivity() {
    private lateinit var timerText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var stopButton: Button
    private lateinit var countDownTimer: CountDownTimer
    
    private val focusDurationMinutes = 25L
    private val focusDurationMillis = focusDurationMinutes * 60 * 1000
    private var timeRemainingMillis = focusDurationMillis
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_focus_mode)
        
        // Keep screen on during focus mode
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        // Initialize views
        timerText = findViewById(R.id.timer_text)
        progressBar = findViewById(R.id.progress_bar)
        stopButton = findViewById(R.id.stop_button)
        
        // Setup progress bar
        progressBar.max = focusDurationMillis.toInt()
        progressBar.progress = focusDurationMillis.toInt()
        
        // Setup stop button
        stopButton.setOnClickListener {
            endFocusMode()
        }
        
        // Start focus mode
        startFocusMode()
    }
    
    private fun startFocusMode() {
        // Enable Do Not Disturb mode
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.isNotificationPolicyAccessGranted) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
        }
        
        // Start countdown timer
        countDownTimer = object : CountDownTimer(timeRemainingMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemainingMillis = millisUntilFinished
                updateTimerUI(millisUntilFinished)
            }
            
            override fun onFinish() {
                endFocusMode()
            }
        }.start()
    }
    
    private fun updateTimerUI(millisUntilFinished: Long) {
        val minutes = millisUntilFinished / 1000 / 60
        val seconds = (millisUntilFinished / 1000) % 60
        timerText.text = String.format("%02d:%02d", minutes, seconds)
        progressBar.progress = millisUntilFinished.toInt()
    }
    
    private fun endFocusMode() {
        // Cancel timer
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        
        // Disable Do Not Disturb mode
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.isNotificationPolicyAccessGranted) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }
        
        // Return to launcher
        finish()
    }
    
    override fun onBackPressed() {
        // Disable back button during focus mode
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}