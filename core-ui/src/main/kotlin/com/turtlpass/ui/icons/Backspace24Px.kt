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

val Backspace24Px: ImageVector
    get() {
        if (_Backspace24Px != null) {
            return _Backspace24Px!!
        }
        _Backspace24Px = ImageVector.Builder(
            name = "Backspace24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
            autoMirror = true
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(560f, 536f)
                lineTo(636f, 612f)
                quadTo(647f, 623f, 664f, 623f)
                quadTo(681f, 623f, 692f, 612f)
                quadTo(703f, 601f, 703f, 584f)
                quadTo(703f, 567f, 692f, 556f)
                lineTo(616f, 480f)
                lineTo(692f, 404f)
                quadTo(703f, 393f, 703f, 376f)
                quadTo(703f, 359f, 692f, 348f)
                quadTo(681f, 337f, 664f, 337f)
                quadTo(647f, 337f, 636f, 348f)
                lineTo(560f, 424f)
                lineTo(484f, 348f)
                quadTo(473f, 337f, 456f, 337f)
                quadTo(439f, 337f, 428f, 348f)
                quadTo(417f, 359f, 417f, 376f)
                quadTo(417f, 393f, 428f, 404f)
                lineTo(504f, 480f)
                lineTo(428f, 556f)
                quadTo(417f, 567f, 417f, 584f)
                quadTo(417f, 601f, 428f, 612f)
                quadTo(439f, 623f, 456f, 623f)
                quadTo(473f, 623f, 484f, 612f)
                lineTo(560f, 536f)
                close()
                moveTo(360f, 800f)
                quadTo(341f, 800f, 324f, 791.5f)
                quadTo(307f, 783f, 296f, 768f)
                lineTo(116f, 528f)
                quadTo(100f, 507f, 100f, 480f)
                quadTo(100f, 453f, 116f, 432f)
                lineTo(296f, 192f)
                quadTo(307f, 177f, 324f, 168.5f)
                quadTo(341f, 160f, 360f, 160f)
                lineTo(800f, 160f)
                quadTo(833f, 160f, 856.5f, 183.5f)
                quadTo(880f, 207f, 880f, 240f)
                lineTo(880f, 720f)
                quadTo(880f, 753f, 856.5f, 776.5f)
                quadTo(833f, 800f, 800f, 800f)
                lineTo(360f, 800f)
                close()
            }
        }.build()

        return _Backspace24Px!!
    }

@Suppress("ObjectPropertyName")
private var _Backspace24Px: ImageVector? = null

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
        Icon(imageVector = Backspace24Px, contentDescription = null)
    }
}
