package com.xerox.studyrays.utils

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat

@Composable
fun SetSystemUiColors(activity: Activity, statusBarColor: Color, navigationBarColor: Color) {
    val window = activity.window
    val useDarkIcons = !isSystemInDarkTheme()

    // Store the original colors
    val originalStatusBarColor = window.statusBarColor
    val originalNavigationBarColor = window.navigationBarColor
    val originalAppearanceLightStatusBars = WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars
    val originalAppearanceLightNavigationBars = WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightNavigationBars

    SideEffect {
        window.statusBarColor = statusBarColor.toArgb()
        window.navigationBarColor = navigationBarColor.toArgb()
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController?.isAppearanceLightStatusBars = useDarkIcons
        insetsController?.isAppearanceLightNavigationBars = useDarkIcons
    }

    DisposableEffect(Unit) {
        onDispose {
            // Reset to the original colors and settings when this composable is disposed
            window.statusBarColor = originalStatusBarColor
            window.navigationBarColor = originalNavigationBarColor
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController?.isAppearanceLightStatusBars = originalAppearanceLightStatusBars ?: useDarkIcons
            insetsController?.isAppearanceLightNavigationBars = originalAppearanceLightNavigationBars ?: useDarkIcons
        }
    }
}