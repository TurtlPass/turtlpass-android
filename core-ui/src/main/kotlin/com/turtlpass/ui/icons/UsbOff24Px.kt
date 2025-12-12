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

val UsbOff24Px: ImageVector
    get() {
        if (_UsbOff24Px != null) {
            return _UsbOff24Px!!
        }
        _UsbOff24Px = ImageVector.Builder(
            name = "UsbOff24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(791f, 905f)
                lineTo(526f, 640f)
                lineTo(520f, 640f)
                lineTo(520f, 732f)
                quadTo(539f, 742f, 549.5f, 760.5f)
                quadTo(560f, 779f, 560f, 800f)
                quadTo(560f, 833f, 536.5f, 856.5f)
                quadTo(513f, 880f, 480f, 880f)
                quadTo(447f, 880f, 423.5f, 856.5f)
                quadTo(400f, 833f, 400f, 800f)
                quadTo(400f, 779f, 411f, 761f)
                quadTo(422f, 743f, 440f, 732f)
                lineTo(440f, 640f)
                lineTo(320f, 640f)
                quadTo(287f, 640f, 263.5f, 616.5f)
                quadTo(240f, 593f, 240f, 560f)
                lineTo(240f, 468f)
                quadTo(221f, 458f, 210.5f, 439.5f)
                quadTo(200f, 421f, 200f, 400f)
                quadTo(200f, 383f, 206.5f, 367.5f)
                quadTo(213f, 352f, 227f, 341f)
                lineTo(55f, 169f)
                lineTo(112f, 112f)
                lineTo(848f, 848f)
                lineTo(791f, 905f)
                close()
                moveTo(711f, 597f)
                lineTo(640f, 526f)
                lineTo(640f, 480f)
                lineTo(600f, 480f)
                lineTo(600f, 480f)
                lineTo(600f, 320f)
                lineTo(760f, 320f)
                lineTo(760f, 480f)
                lineTo(720f, 480f)
                lineTo(720f, 560f)
                quadTo(720f, 570f, 718f, 579.5f)
                quadTo(716f, 589f, 711f, 597f)
                close()
                moveTo(320f, 560f)
                lineTo(440f, 560f)
                lineTo(440f, 554f)
                lineTo(339f, 453f)
                quadTo(335f, 458f, 330f, 461.5f)
                quadTo(325f, 465f, 320f, 468f)
                lineTo(320f, 560f)
                quadTo(320f, 560f, 320f, 560f)
                quadTo(320f, 560f, 320f, 560f)
                close()
                moveTo(520f, 406f)
                lineTo(440f, 326f)
                lineTo(440f, 240f)
                lineTo(360f, 240f)
                lineTo(360f, 240f)
                lineTo(480f, 80f)
                lineTo(600f, 240f)
                lineTo(520f, 240f)
                lineTo(520f, 406f)
                close()
            }
        }.build()

        return _UsbOff24Px!!
    }

@Suppress("ObjectPropertyName")
private var _UsbOff24Px: ImageVector? = null

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
        Icon(imageVector = UsbOff24Px, contentDescription = null)
    }
}
