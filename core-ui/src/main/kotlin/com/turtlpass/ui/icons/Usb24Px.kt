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

val Usb24Px: ImageVector
    get() {
        if (_Usb24Px != null) {
            return _Usb24Px!!
        }
        _Usb24Px = ImageVector.Builder(
            name = "Usb24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(480f, 880f)
                quadTo(447f, 880f, 423.5f, 856.5f)
                quadTo(400f, 833f, 400f, 800f)
                quadTo(400f, 779f, 411f, 761f)
                quadTo(422f, 743f, 440f, 732f)
                lineTo(440f, 640f)
                lineTo(320f, 640f)
                quadTo(287f, 640f, 263.5f, 616.5f)
                quadTo(240f, 593f, 240f, 560f)
                lineTo(240f, 468f)
                quadTo(222f, 459f, 211f, 441f)
                quadTo(200f, 423f, 200f, 400f)
                quadTo(200f, 367f, 223.5f, 343.5f)
                quadTo(247f, 320f, 280f, 320f)
                quadTo(313f, 320f, 336.5f, 343.5f)
                quadTo(360f, 367f, 360f, 400f)
                quadTo(360f, 423f, 349f, 440f)
                quadTo(338f, 457f, 320f, 468f)
                lineTo(320f, 560f)
                quadTo(320f, 560f, 320f, 560f)
                quadTo(320f, 560f, 320f, 560f)
                lineTo(440f, 560f)
                lineTo(440f, 240f)
                lineTo(360f, 240f)
                lineTo(480f, 80f)
                lineTo(600f, 240f)
                lineTo(520f, 240f)
                lineTo(520f, 560f)
                lineTo(640f, 560f)
                quadTo(640f, 560f, 640f, 560f)
                quadTo(640f, 560f, 640f, 560f)
                lineTo(640f, 480f)
                lineTo(600f, 480f)
                lineTo(600f, 320f)
                lineTo(760f, 320f)
                lineTo(760f, 480f)
                lineTo(720f, 480f)
                lineTo(720f, 560f)
                quadTo(720f, 593f, 696.5f, 616.5f)
                quadTo(673f, 640f, 640f, 640f)
                lineTo(520f, 640f)
                lineTo(520f, 732f)
                quadTo(539f, 742f, 549.5f, 760f)
                quadTo(560f, 778f, 560f, 800f)
                quadTo(560f, 833f, 536.5f, 856.5f)
                quadTo(513f, 880f, 480f, 880f)
                close()
            }
        }.build()

        return _Usb24Px!!
    }

@Suppress("ObjectPropertyName")
private var _Usb24Px: ImageVector? = null

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
        Icon(imageVector = Usb24Px, contentDescription = null)
    }
}
