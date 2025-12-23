package com.turtlpass.ui.statusBar

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.core.view.WindowCompat

@Composable
fun StatusBarColors(darkIcons: Boolean) {
    val activity = LocalActivity.current as ComponentActivity

    SideEffect {
        activity.enableEdgeToEdge()

        // Set system bar colors using WindowInsetsControllerCompat
        val window = activity.window
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = darkIcons
        }
    }
}
