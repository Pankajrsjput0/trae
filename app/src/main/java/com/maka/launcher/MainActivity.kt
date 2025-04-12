package com.maka.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maka.launcher.ui.screens.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MakaApp()
        }
    }
}

@Composable
fun MakaApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val themeViewModel: ThemeViewModel = hiltViewModel()
    
    val authState by authViewModel.authState.collectAsState()
    val settings by settingsViewModel.settings.collectAsState()
    val currentTheme by themeViewModel.currentTheme.collectAsState()

    MaterialTheme(
        colorScheme = currentTheme.toColorScheme(),
        typography = AppTypography,
        shapes = AppShapes
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = if (authState is AuthState.Authenticated) "home" else "auth"
            ) {
                composable("auth") {
                    AuthScreen(
                        onAuthSuccess = {
                            navController.navigate("home") {
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                    )
                }
                composable("home") { HomeScreen(navController) }
                composable("app_drawer") { AppDrawerScreen(navController) }
                composable("pomodoro") { PomodoroScreen(navController) }
                composable("profile") { ProfileScreen(navController) }
                composable("restricted_apps") { /* RestrictedAppsScreen(navController) */ }
                composable("rewarded_ad") {
                    RewardedAdScreen(
                        onAdComplete = { minutes ->
                            // Handle rewarded minutes
                            navController.popBackStack()
                        }
                    )
                }
                composable("theme_settings") {
                    ThemeSettingsScreen(
                        navController = navController,
                        viewModel = themeViewModel
                    )
                }
            }
        }
    }
}