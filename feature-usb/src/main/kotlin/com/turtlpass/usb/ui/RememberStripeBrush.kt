package com.turtlpass.usb.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.turtlpass.usb.model.UsbDeviceUiState

@Composable
fun rememberStripeBrush(
    state: UsbDeviceUiState
): Brush {
    val density = LocalDensity.current

    // Density-aware stripe width
    val stripeWidthPx = remember(density) {
        with(density) { 20.dp.toPx() }
    }

    // Target colors
    val targetPrimary = state.primaryColorUsbDevice()
    val targetSecondary = state.secondaryColorUsbDevice()

    // Color animations
    val primaryColor by animateColorAsState(targetPrimary, label = "primaryColor")
    val secondaryColor by animateColorAsState(targetSecondary, label = "secondaryColor")

    // Cache brush creation
    return remember(primaryColor, secondaryColor, stripeWidthPx) {
        Brush.linearGradient(
            colorStops = arrayOf(
                0f to primaryColor,
                0.5f to primaryColor,
                0.5f to secondaryColor,
                1f to secondaryColor
            ),
            start = Offset.Zero,
            end = Offset(stripeWidthPx, stripeWidthPx),
            tileMode = TileMode.Repeated
        )
    }
}
