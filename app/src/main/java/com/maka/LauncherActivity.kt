package com.maka

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class LauncherActivity : AppCompatActivity() {
    private lateinit var timeText: TextView
    private lateinit var dateText: TextView
    private lateinit var pomodoroButton: Button
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var gestureDetector: GestureDetector
    
    private val timeUpdateRunnable = object : Runnable {
        override fun run() {
            updateDateTime()
            handler.postDelayed(this, 1000)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        
        // Initialize views
        timeText = findViewById(R.id.time_text)
        dateText = findViewById(R.id.date_text)
        pomodoroButton = findViewById(R.id.pomodoro_button)
        
        // Setup Pomodoro button
        pomodoroButton.setOnClickListener {
            startActivity(Intent(this, FocusModeActivity::class.java))
        }
        
        // Setup gesture detector
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e1 == null) return false
                
                val diffY = e2.y - e1.y
                if (Math.abs(diffY) > 100) {
                    if (diffY > 0) {
                        // Swipe down - show notifications
                        openNotificationShade()
                    } else {
                        // Swipe up - show app drawer
                        openAppDrawer()
                    }
                    return true
                }
                return false
            }
            
            override fun onDoubleTap(e: MotionEvent): Boolean {
                // Double tap - lock screen
                lockScreen()
                return true
            }
        })
        
        // Start time updates
        handler.post(timeUpdateRunnable)
    }
    
    private fun updateDateTime() {
        val calendar = Calendar.getInstance()
        
        // Update time
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        timeText.text = timeFormat.format(calendar.time)
        
        // Update date
        val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        dateText.text = dateFormat.format(calendar.time)
    }
    
    private fun openNotificationShade() {
        try {
            // This requires system permissions
            val statusBarService = getSystemService("statusbar")
            val statusBarManager = Class.forName("android.app.StatusBarManager")
            val expandMethod = statusBarManager.getMethod("expandNotificationsPanel")
            expandMethod.invoke(statusBarService)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun openAppDrawer() {
        // TODO: Implement app drawer activity
    }
    
    private fun lockScreen() {
        // This requires device admin permissions
        val devicePolicyManager = getSystemService(DEVICE_POLICY_SERVICE) as android.app.admin.DevicePolicyManager
        try {
            devicePolicyManager.lockNow()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeUpdateRunnable)
    }
}