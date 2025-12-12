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

val ExitToApp24Px: ImageVector
    get() {
        if (_ExitToApp24Px != null) {
            return _ExitToApp24Px!!
        }
        _ExitToApp24Px = ImageVector.Builder(
            name = "ExitToApp24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
            autoMirror = true
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(200f, 840f)
                quadTo(167f, 840f, 143.5f, 816.5f)
                quadTo(120f, 793f, 120f, 760f)
                lineTo(120f, 640f)
                quadTo(120f, 623f, 131.5f, 611.5f)
                quadTo(143f, 600f, 160f, 600f)
                quadTo(177f, 600f, 188.5f, 611.5f)
                quadTo(200f, 623f, 200f, 640f)
                lineTo(200f, 760f)
                quadTo(200f, 760f, 200f, 760f)
                quadTo(200f, 760f, 200f, 760f)
                lineTo(760f, 760f)
                quadTo(760f, 760f, 760f, 760f)
                quadTo(760f, 760f, 760f, 760f)
                lineTo(760f, 200f)
                quadTo(760f, 200f, 760f, 200f)
                quadTo(760f, 200f, 760f, 200f)
                lineTo(200f, 200f)
                quadTo(200f, 200f, 200f, 200f)
                quadTo(200f, 200f, 200f, 200f)
                lineTo(200f, 320f)
                quadTo(200f, 337f, 188.5f, 348.5f)
                quadTo(177f, 360f, 160f, 360f)
                quadTo(143f, 360f, 131.5f, 348.5f)
                quadTo(120f, 337f, 120f, 320f)
                lineTo(120f, 200f)
                quadTo(120f, 167f, 143.5f, 143.5f)
                quadTo(167f, 120f, 200f, 120f)
                lineTo(760f, 120f)
                quadTo(793f, 120f, 816.5f, 143.5f)
                quadTo(840f, 167f, 840f, 200f)
                lineTo(840f, 760f)
                quadTo(840f, 793f, 816.5f, 816.5f)
                quadTo(793f, 840f, 760f, 840f)
                lineTo(200f, 840f)
                close()
                moveTo(466f, 520f)
                lineTo(160f, 520f)
                quadTo(143f, 520f, 131.5f, 508.5f)
                quadTo(120f, 497f, 120f, 480f)
                quadTo(120f, 463f, 131.5f, 451.5f)
                quadTo(143f, 440f, 160f, 440f)
                lineTo(466f, 440f)
                lineTo(392f, 366f)
                quadTo(380f, 354f, 380.5f, 338f)
                quadTo(381f, 322f, 392f, 310f)
                quadTo(404f, 298f, 420.5f, 297.5f)
                quadTo(437f, 297f, 449f, 309f)
                lineTo(592f, 452f)
                quadTo(598f, 458f, 600.5f, 465f)
                quadTo(603f, 472f, 603f, 480f)
                quadTo(603f, 488f, 600.5f, 495f)
                quadTo(598f, 502f, 592f, 508f)
                lineTo(449f, 651f)
                quadTo(437f, 663f, 420.5f, 662.5f)
                quadTo(404f, 662f, 392f, 650f)
                quadTo(381f, 638f, 380.5f, 622f)
                quadTo(380f, 606f, 392f, 594f)
                lineTo(466f, 520f)
                close()
            }
        }.build()

        return _ExitToApp24Px!!
    }

@Suppress("ObjectPropertyName")
private var _ExitToApp24Px: ImageVector? = null

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
        Icon(imageVector = ExitToApp24Px, contentDescription = null)
    }
}
