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

val KeyVertical24Px: ImageVector
    get() {
        if (_KeyVertical24Px != null) {
            return _KeyVertical24Px!!
        }
        _KeyVertical24Px = ImageVector.Builder(
            name = "KeyVertical24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(420f, 280f)
                quadTo(420f, 247f, 443.5f, 223.5f)
                quadTo(467f, 200f, 500f, 200f)
                quadTo(533f, 200f, 556.5f, 223.5f)
                quadTo(580f, 247f, 580f, 280f)
                quadTo(580f, 313f, 556.5f, 336.5f)
                quadTo(533f, 360f, 500f, 360f)
                quadTo(467f, 360f, 443.5f, 336.5f)
                quadTo(420f, 313f, 420f, 280f)
                close()
                moveTo(500f, 960f)
                lineTo(320f, 780f)
                lineTo(380f, 700f)
                lineTo(320f, 620f)
                lineTo(380f, 535f)
                lineTo(380f, 488f)
                quadTo(326f, 456f, 293f, 401.5f)
                quadTo(260f, 347f, 260f, 280f)
                quadTo(260f, 180f, 330f, 110f)
                quadTo(400f, 40f, 500f, 40f)
                quadTo(600f, 40f, 670f, 110f)
                quadTo(740f, 180f, 740f, 280f)
                quadTo(740f, 347f, 707f, 401.5f)
                quadTo(674f, 456f, 620f, 488f)
                lineTo(620f, 840f)
                lineTo(500f, 960f)
                close()
                moveTo(340f, 280f)
                quadTo(340f, 336f, 374f, 378.5f)
                quadTo(408f, 421f, 460f, 435f)
                lineTo(460f, 560f)
                lineTo(419f, 618f)
                lineTo(480f, 700f)
                lineTo(425f, 771f)
                lineTo(500f, 846f)
                lineTo(540f, 806f)
                lineTo(540f, 435f)
                quadTo(592f, 421f, 626f, 378.5f)
                quadTo(660f, 336f, 660f, 280f)
                quadTo(660f, 214f, 613f, 167f)
                quadTo(566f, 120f, 500f, 120f)
                quadTo(434f, 120f, 387f, 167f)
                quadTo(340f, 214f, 340f, 280f)
                close()
            }
        }.build()

        return _KeyVertical24Px!!
    }

@Suppress("ObjectPropertyName")
private var _KeyVertical24Px: ImageVector? = null

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
        Icon(imageVector = KeyVertical24Px, contentDescription = null)
    }
}
