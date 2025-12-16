package com.turtlpass.biometric.ui

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import kotlinx.coroutines.delay

@Composable
fun PinIndicator(
    modifier: Modifier = Modifier,
    index: Int,
    active: Boolean
) {
    val animProgress = remember { Animatable(0f) }
    val stagger = index * 25L

    // Shapes
    val starPolygon = remember {
        RoundedPolygon.star(
            numVerticesPerRadius = 12,
            innerRadius = 1f / 3f,
            rounding = CornerRounding(1f / 6f)
        )
    }
    val circlePolygon = remember {
        RoundedPolygon.circle(numVertices = 12)
    }
    val morph = remember { Morph(circlePolygon, starPolygon) }

    // Reusable paths & matrix
    val androidPath = remember { android.graphics.Path() }
    var composePath = remember { androidx.compose.ui.graphics.Path() }
    val matrix = remember { Matrix() }

    LaunchedEffect(active) {
        if (active) {
            delay(stagger)
            animProgress.snapTo(0f)
            animProgress.animateTo(1f, tween(120, easing = LinearEasing))
            animProgress.animateTo(0f, tween(120, easing = LinearEasing))
        } else {
            animProgress.snapTo(0f)
        }
    }

    val color = if (active) colors.default.accent else colors.default.unfocused

    Canvas(
        modifier
            // Allow drawing outside bounds so we can scale UP without clipping
            .graphicsLayer { clip = false }
            .size(dimensions.x32 + dimensions.x4)
    ) {
        // Get Android Path
        morph.toPath(animProgress.value, androidPath)

        // Convert to Compose Path
        composePath = androidPath.asComposePath()

        // Matrix Calculations
        matrix.reset()

        // multiply by roughly 0.5f to leave room for the "pop" expansion
        val baseRadius = size.minDimension / 2f * 0.5f

        // Scaling
        val dynamicScale = 1f + (2.0f * animProgress.value)
        val finalScale = baseRadius * dynamicScale
        matrix.scale(finalScale, finalScale)
        composePath.transform(matrix)

        // Draw
        withTransform({
            translate(center.x, center.y)
        }) {
            drawPath(composePath, color)
        }
    }
}


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
    backgroundColor = 0xff424242,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Composable
private fun Preview() {
    AppTheme {
        Row {
            PinIndicator(index = 0, active = true)
            PinIndicator(index = 1, active = true)
            PinIndicator(index = 2, active = true)
            PinIndicator(index = 3, active = false)
            PinIndicator(index = 4, active = false)
            PinIndicator(index = 5, active = false)
        }
    }
}
