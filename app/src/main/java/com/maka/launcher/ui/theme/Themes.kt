package com.maka.launcher.ui.theme

import androidx.compose.ui.graphics.Color

val DefaultThemes = listOf(
    ThemeConfig(
        name = "Classic",
        isDark = false,
        primary = Color(0xFF6200EE),
        secondary = Color(0xFF03DAC6),
        tertiary = Color(0xFF3700B3),
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFF5F5F5)
    ),
    ThemeConfig(
        name = "Ocean",
        isDark = false,
        primary = Color(0xFF006064),
        secondary = Color(0xFF00ACC1),
        tertiary = Color(0xFF00838F),
        background = Color(0xFFE0F7FA),
        surface = Color(0xFFB2EBF2)
    ),
    ThemeConfig(
        name = "Forest",
        isDark = false,
        primary = Color(0xFF2E7D32),
        secondary = Color(0xFF66BB6A),
        tertiary = Color(0xFF1B5E20),
        background = Color(0xFFE8F5E9),
        surface = Color(0xFFC8E6C9)
    ),
    ThemeConfig(
        name = "Midnight",
        isDark = true,
        primary = Color(0xFF BB86FC),
        secondary = Color(0xFF03DAC6),
        tertiary = Color(0xFF3700B3),
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E)
    )
)