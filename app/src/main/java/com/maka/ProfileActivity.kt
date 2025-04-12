package com.maka

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class ProfileActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var coinBalanceText: TextView
    private lateinit var themeSpinner: Spinner
    private lateinit var fontSizeSpinner: Spinner
    private lateinit var fontStyleSpinner: Spinner
    private lateinit var grayscaleSwitch: Switch
    private lateinit var logoutButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        
        // Initialize views
        coinBalanceText = findViewById(R.id.coin_balance_text)
        themeSpinner = findViewById(R.id.theme_spinner)
        fontSizeSpinner = findViewById(R.id.font_size_spinner)
        fontStyleSpinner = findViewById(R.id.font_style_spinner)
        grayscaleSwitch = findViewById(R.id.grayscale_switch)
        logoutButton = findViewById(R.id.logout_button)
        
        // Load current settings
        loadSettings()
        
        // Setup listeners
        setupSettingsListeners()
        
        // Setup logout button
        logoutButton.setOnClickListener {
            logout()
        }
    }
    
    private fun loadSettings() {
        // Load coin balance
        val coinBalance = preferences.getInt("coin_balance", 0)
        coinBalanceText.text = getString(R.string.coin_balance_format, coinBalance)
        
        // Load theme setting
        val currentTheme = preferences.getString("theme", "light")
        themeSpinner.setSelection(getThemePosition(currentTheme))
        
        // Load font size
        val currentFontSize = preferences.getString("font_size", "medium")
        fontSizeSpinner.setSelection(getFontSizePosition(currentFontSize))
        
        // Load font style
        val currentFontStyle = preferences.getString("font_style", "default")
        fontStyleSpinner.setSelection(getFontStylePosition(currentFontStyle))
        
        // Load grayscale setting
        val isGrayscale = preferences.getBoolean("grayscale", false)
        grayscaleSwitch.isChecked = isGrayscale
    }
    
    private fun setupSettingsListeners() {
        themeSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val theme = getThemeValue(position)
                preferences.edit().putString("theme", theme).apply()
                applyTheme(theme)
            }
            
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
        
        fontSizeSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val fontSize = getFontSizeValue(position)
                preferences.edit().putString("font_size", fontSize).apply()
                applyFontSize(fontSize)
            }
            
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
        
        fontStyleSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val fontStyle = getFontStyleValue(position)
                preferences.edit().putString("font_style", fontStyle).apply()
                applyFontStyle(fontStyle)
            }
            
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
        
        grayscaleSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("grayscale", isChecked).apply()
            applyGrayscale(isChecked)
        }
    }
    
    private fun getThemePosition(theme: String?): Int {
        return when (theme) {
            "light" -> 0
            "dark" -> 1
            else -> 0
        }
    }
    
    private fun getThemeValue(position: Int): String {
        return when (position) {
            0 -> "light"
            1 -> "dark"
            else -> "light"
        }
    }
    
    private fun getFontSizePosition(fontSize: String?): Int {
        return when (fontSize) {
            "small" -> 0
            "medium" -> 1
            "large" -> 2
            else -> 1
        }
    }
    
    private fun getFontSizeValue(position: Int): String {
        return when (position) {
            0 -> "small"
            1 -> "medium"
            2 -> "large"
            else -> "medium"
        }
    }
    
    private fun getFontStylePosition(fontStyle: String?): Int {
        return when (fontStyle) {
            "default" -> 0
            "serif" -> 1
            "sans-serif" -> 2
            else -> 0
        }
    }
    
    private fun getFontStyleValue(position: Int): String {
        return when (position) {
            0 -> "default"
            1 -> "serif"
            2 -> "sans-serif"
            else -> "default"
        }
    }
    
    private fun applyTheme(theme: String) {
        // TODO: Implement theme application
    }
    
    private fun applyFontSize(fontSize: String) {
        // TODO: Implement font size application
    }
    
    private fun applyFontStyle(fontStyle: String) {
        // TODO: Implement font style application
    }
    
    private fun applyGrayscale(enabled: Boolean) {
        // TODO: Implement grayscale mode
    }
    
    private fun logout() {
        // Clear user preferences
        preferences.edit().clear().apply()
        
        // Return to login/welcome screen
        // TODO: Implement proper navigation
        finish()
    }
}