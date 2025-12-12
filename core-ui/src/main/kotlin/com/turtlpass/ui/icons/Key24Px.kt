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

val Key24Px: ImageVector
    get() {
        if (_Key24Px != null) {
            return _Key24Px!!
        }
        _Key24Px = ImageVector.Builder(
            name = "Key24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(280f, 560f)
                quadTo(247f, 560f, 223.5f, 536.5f)
                quadTo(200f, 513f, 200f, 480f)
                quadTo(200f, 447f, 223.5f, 423.5f)
                quadTo(247f, 400f, 280f, 400f)
                quadTo(313f, 400f, 336.5f, 423.5f)
                quadTo(360f, 447f, 360f, 480f)
                quadTo(360f, 513f, 336.5f, 536.5f)
                quadTo(313f, 560f, 280f, 560f)
                close()
                moveTo(280f, 720f)
                quadTo(180f, 720f, 110f, 650f)
                quadTo(40f, 580f, 40f, 480f)
                quadTo(40f, 380f, 110f, 310f)
                quadTo(180f, 240f, 280f, 240f)
                quadTo(347f, 240f, 401.5f, 273f)
                quadTo(456f, 306f, 488f, 360f)
                lineTo(840f, 360f)
                lineTo(960f, 480f)
                lineTo(780f, 660f)
                lineTo(700f, 600f)
                lineTo(620f, 660f)
                lineTo(535f, 600f)
                lineTo(488f, 600f)
                quadTo(456f, 654f, 401.5f, 687f)
                quadTo(347f, 720f, 280f, 720f)
                close()
                moveTo(280f, 640f)
                quadTo(336f, 640f, 378.5f, 606f)
                quadTo(421f, 572f, 435f, 520f)
                lineTo(560f, 520f)
                lineTo(618f, 561f)
                lineTo(700f, 500f)
                lineTo(771f, 555f)
                lineTo(846f, 480f)
                lineTo(806f, 440f)
                lineTo(435f, 440f)
                quadTo(421f, 388f, 378.5f, 354f)
                quadTo(336f, 320f, 280f, 320f)
                quadTo(214f, 320f, 167f, 367f)
                quadTo(120f, 414f, 120f, 480f)
                quadTo(120f, 546f, 167f, 593f)
                quadTo(214f, 640f, 280f, 640f)
                close()
            }
        }.build()

        return _Key24Px!!
    }

@Suppress("ObjectPropertyName")
private var _Key24Px: ImageVector? = null

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
        Icon(imageVector = Key24Px, contentDescription = null)
    }
}
