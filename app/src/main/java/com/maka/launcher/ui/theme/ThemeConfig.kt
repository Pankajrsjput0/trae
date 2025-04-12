package com.maka.launcher.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

data class ThemeConfig(
    val name: String,
    val isDark: Boolean,
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val background: Color,
    val surface: Color
) {
    fun toColorScheme(): ColorScheme {
        return if (isDark) {
            darkColorScheme(
                primary = primary,
                secondary = secondary,
                tertiary = tertiary,
                background = background,
                surface = surface
            )
        } else {
            lightColorScheme(
                primary = primary,
                secondary = secondary,
                tertiary = tertiary,
                background = background,
                surface = surface
            )
        }
    }
}