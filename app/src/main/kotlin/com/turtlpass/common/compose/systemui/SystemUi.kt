package com.turtlpass.common.compose.systemui

import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SystemUi(
    window: Window,
    systemUiController: SystemUiController = rememberSystemUiController(),
    isNotDarkTheme: Boolean = isSystemInDarkTheme().not(),
    content: @Composable () -> Unit
) {
    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        with(systemUiController) {
            setStatusBarColor(
                color = Color.Transparent,
                darkIcons = isNotDarkTheme
            )
            setNavigationBarColor(
                color = Color.Transparent,
                darkIcons = isNotDarkTheme,
                navigationBarContrastEnforced = true
            )
        }
    }
    content()
}
