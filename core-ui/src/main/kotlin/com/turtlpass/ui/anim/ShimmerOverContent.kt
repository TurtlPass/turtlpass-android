package com.turtlpass.ui.anim

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.lerp
import kotlin.math.max


fun Modifier.shimmerOverContent(
    highlightColor: Color,
    progressForMaxAlpha: Float = 0.6f,
): Modifier = composed {

    val transition = rememberInfiniteTransition(label = "shimmer")

    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = ShimmerDefaults.shimmerAnimationSpec.animation,
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerProgress"
    )

    this.drawWithContent {
        // Draw icon / image first
        drawContent()

        // Build shimmer brush
        val radius = (max(size.width, size.height) * progress * 2)
            .coerceAtLeast(0.01f)

        val brush = Brush.radialGradient(
            colors = listOf(
                highlightColor.copy(alpha = 0f),
                highlightColor,
                highlightColor.copy(alpha = 0f),
            ),
            center = Offset(0f, 0f),
            radius = radius
        )

        // Alpha curve
        val alpha = if (progress <= progressForMaxAlpha) {
            lerp(0f, 1f, progress / progressForMaxAlpha)
        } else {
            lerp(
                1f,
                0f,
                (progress - progressForMaxAlpha) / (1f - progressForMaxAlpha)
            )
        }

        // Draw shimmer OVER content
        drawRect(
            brush = brush,
            alpha = alpha
        )
    }
}
