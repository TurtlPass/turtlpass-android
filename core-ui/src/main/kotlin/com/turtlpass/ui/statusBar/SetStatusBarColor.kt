package com.turtlpass.ui.statusBar

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat

@Composable
fun SetStatusBarColor(color: Color, darkIcons: Boolean = true) {
    val activity = LocalActivity.current as ComponentActivity

    // Enable edge-to-edge layout
    SideEffect {
        activity.enableEdgeToEdge()

        // Set system bar colors using WindowInsetsControllerCompat
        val window = activity.window
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = darkIcons
        }
    }

    // Draw a background behind the status bar
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(statusBarHeight)
            .background(color)
    )
}
