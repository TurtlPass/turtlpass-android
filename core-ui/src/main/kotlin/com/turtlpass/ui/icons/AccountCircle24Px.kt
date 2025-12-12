package com.turtlpass.ui.icons

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.turtlpass.ui.theme.AppTheme

val AccountCircle24Px: ImageVector
    get() {
        if (_AccountCircle24Px != null) {
            return _AccountCircle24Px!!
        }
        _AccountCircle24Px = ImageVector.Builder(
            name = "AccountCircle24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(234f, 684f)
                quadTo(285f, 645f, 348f, 622.5f)
                quadTo(411f, 600f, 480f, 600f)
                quadTo(549f, 600f, 612f, 622.5f)
                quadTo(675f, 645f, 726f, 684f)
                quadTo(761f, 643f, 780.5f, 591f)
                quadTo(800f, 539f, 800f, 480f)
                quadTo(800f, 347f, 706.5f, 253.5f)
                quadTo(613f, 160f, 480f, 160f)
                quadTo(347f, 160f, 253.5f, 253.5f)
                quadTo(160f, 347f, 160f, 480f)
                quadTo(160f, 539f, 179.5f, 591f)
                quadTo(199f, 643f, 234f, 684f)
                close()
                moveTo(480f, 520f)
                quadTo(421f, 520f, 380.5f, 479.5f)
                quadTo(340f, 439f, 340f, 380f)
                quadTo(340f, 321f, 380.5f, 280.5f)
                quadTo(421f, 240f, 480f, 240f)
                quadTo(539f, 240f, 579.5f, 280.5f)
                quadTo(620f, 321f, 620f, 380f)
                quadTo(620f, 439f, 579.5f, 479.5f)
                quadTo(539f, 520f, 480f, 520f)
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
                moveTo(480f, 800f)
                quadTo(533f, 800f, 580f, 784.5f)
                quadTo(627f, 769f, 666f, 740f)
                quadTo(627f, 711f, 580f, 695.5f)
                quadTo(533f, 680f, 480f, 680f)
                quadTo(427f, 680f, 380f, 695.5f)
                quadTo(333f, 711f, 294f, 740f)
                quadTo(333f, 769f, 380f, 784.5f)
                quadTo(427f, 800f, 480f, 800f)
                close()
                moveTo(480f, 440f)
                quadTo(506f, 440f, 523f, 423f)
                quadTo(540f, 406f, 540f, 380f)
                quadTo(540f, 354f, 523f, 337f)
                quadTo(506f, 320f, 480f, 320f)
                quadTo(454f, 320f, 437f, 337f)
                quadTo(420f, 354f, 420f, 380f)
                quadTo(420f, 406f, 437f, 423f)
                quadTo(454f, 440f, 480f, 440f)
                close()
                moveTo(480f, 380f)
                quadTo(480f, 380f, 480f, 380f)
                quadTo(480f, 380f, 480f, 380f)
                quadTo(480f, 380f, 480f, 380f)
                quadTo(480f, 380f, 480f, 380f)
                quadTo(480f, 380f, 480f, 380f)
                quadTo(480f, 380f, 480f, 380f)
                quadTo(480f, 380f, 480f, 380f)
                quadTo(480f, 380f, 480f, 380f)
                close()
                moveTo(480f, 740f)
                quadTo(480f, 740f, 480f, 740f)
                quadTo(480f, 740f, 480f, 740f)
                quadTo(480f, 740f, 480f, 740f)
                quadTo(480f, 740f, 480f, 740f)
                quadTo(480f, 740f, 480f, 740f)
                quadTo(480f, 740f, 480f, 740f)
                quadTo(480f, 740f, 480f, 740f)
                quadTo(480f, 740f, 480f, 740f)
                close()
            }
        }.build()

        return _AccountCircle24Px!!
    }

@Suppress("ObjectPropertyName")
private var _AccountCircle24Px: ImageVector? = null

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
        Icon(imageVector = AccountCircle24Px, contentDescription = null)
    }
}
