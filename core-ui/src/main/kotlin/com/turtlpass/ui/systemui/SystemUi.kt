package com.turtlpass.ui.systemui

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SystemUi(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    statusBarColor: Color = Color.Transparent,
    navigationBarColor: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val activity = view.context as? ComponentActivity ?: return
    val window = activity.window ?: return

    SideEffect {
        // Enables edge-to-edge content rendering
        activity.enableEdgeToEdge(
            statusBarStyle = if (isDarkTheme) {
                // Light icons on dark background
                SystemBarStyle.dark(
                    scrim = statusBarColor.toArgb()
                )
            } else {
                // Dark icons on light background
                SystemBarStyle.light(
                    scrim = statusBarColor.toArgb(),
                    darkScrim = Color.Black.toArgb()
                )
            },
            navigationBarStyle = if (isDarkTheme) {
                SystemBarStyle.dark(scrim = navigationBarColor.toArgb())
            } else {
                SystemBarStyle.light(
                    scrim = navigationBarColor.toArgb(),
                    darkScrim = Color.Black.toArgb()
                )
            }
        )
        // allow content behind system bars
        WindowCompat.setDecorFitsSystemWindows(window, true)
    }
    content()
}
