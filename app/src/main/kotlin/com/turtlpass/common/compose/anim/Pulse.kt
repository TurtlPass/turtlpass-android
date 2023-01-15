package com.turtlpass.common.compose.anim

import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun Pulse(
    modifier: Modifier,
    tint: Color,
    enabled: Boolean = true,
    bounded: Boolean = true,
    circleCount: Int = 9,
    duration: Int = 1000,
    initialValue: Float = 0f,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulses = List(circleCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = initialValue,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(duration * circleCount, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(index * duration),
            )
        )
    }
    Canvas(modifier.then(if (bounded) Modifier.clipToBounds() else Modifier)) {
        val radius = sqrt(size.width.pow(2) + size.height.pow(2)) / 2
        if (enabled) {
            pulses.forEach {
                val progress = it.value
                scale(progress) {
                    drawCircle(
                        color = tint,
                        alpha = (1f - progress) * 0.25f,
                        radius = radius
                    )
                }
            }
        }
    }
}


@ExperimentalPermissionsApi
@Preview(
    name = "Light theme",
    showBackground = true,
    backgroundColor = 0xffffffff,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Preview(
    name = "Dark theme",
    showBackground = true,
    backgroundColor = 0xff303030,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Composable
private fun Preview() {
    AppTheme {
        Pulse(
            modifier = Modifier.size(256.dp),
            tint = colors.default.accent,
            initialValue = 0.1f,
        )
    }
}
