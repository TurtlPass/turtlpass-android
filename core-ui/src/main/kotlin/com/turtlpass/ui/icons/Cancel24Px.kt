package com.turtlpass.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.turtlpass.ui.theme.AppTheme

val Cancel24Px: ImageVector
    get() {
        if (_Cancel24Px != null) {
            return _Cancel24Px!!
        }
        _Cancel24Px = ImageVector.Builder(
            name = "Cancel24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(480f, 536f)
                lineTo(596f, 652f)
                quadTo(607f, 663f, 624f, 663f)
                quadTo(641f, 663f, 652f, 652f)
                quadTo(663f, 641f, 663f, 624f)
                quadTo(663f, 607f, 652f, 596f)
                lineTo(536f, 480f)
                lineTo(652f, 364f)
                quadTo(663f, 353f, 663f, 336f)
                quadTo(663f, 319f, 652f, 308f)
                quadTo(641f, 297f, 624f, 297f)
                quadTo(607f, 297f, 596f, 308f)
                lineTo(480f, 424f)
                lineTo(364f, 308f)
                quadTo(353f, 297f, 336f, 297f)
                quadTo(319f, 297f, 308f, 308f)
                quadTo(297f, 319f, 297f, 336f)
                quadTo(297f, 353f, 308f, 364f)
                lineTo(424f, 480f)
                lineTo(308f, 596f)
                quadTo(297f, 607f, 297f, 624f)
                quadTo(297f, 641f, 308f, 652f)
                quadTo(319f, 663f, 336f, 663f)
                quadTo(353f, 663f, 364f, 652f)
                lineTo(480f, 536f)
                close()
                moveTo(480f, 880f)
                quadTo(397f, 880f, 324f, 848.5f)
                quadTo(251f, 817f, 197f, 763f)
                quadTo(143f, 709f, 111.5f, 636f)
                quadTo(80f, 563f, 80f, 480f)
                quadTo(80f, 397f, 111.5f, 324f)
                quadTo(143f, 251f, 197f, 197f)
                quadTo(251f, 143f, 324f, 111.5f)
                quadTo(397f, 80f, 480f, 80f)
                quadTo(563f, 80f, 636f, 111.5f)
                quadTo(709f, 143f, 763f, 197f)
                quadTo(817f, 251f, 848.5f, 324f)
                quadTo(880f, 397f, 880f, 480f)
                quadTo(880f, 563f, 848.5f, 636f)
                quadTo(817f, 709f, 763f, 763f)
                quadTo(709f, 817f, 636f, 848.5f)
                quadTo(563f, 880f, 480f, 880f)
                close()
            }
        }.build()

        return _Cancel24Px!!
    }

@Suppress("ObjectPropertyName")
private var _Cancel24Px: ImageVector? = null

@Preview(
    name = "Light theme",
    widthDp = 128,
    heightDp = 128,
    showBackground = true,
    backgroundColor = 0xffffffff,
)
@Composable
private fun Preview() {
    AppTheme {
        Icon(imageVector = Cancel24Px, contentDescription = null)
    }
}
