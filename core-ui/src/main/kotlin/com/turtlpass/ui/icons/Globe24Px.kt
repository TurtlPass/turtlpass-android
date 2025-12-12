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

val Globe24Px: ImageVector
    get() {
        if (_Globe24Px != null) {
            return _Globe24Px!!
        }
        _Globe24Px = ImageVector.Builder(
            name = "Globe24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
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
                quadTo(614f, 800f, 707f, 707f)
                quadTo(800f, 614f, 800f, 480f)
                quadTo(800f, 473f, 799.5f, 465.5f)
                quadTo(799f, 458f, 799f, 453f)
                quadTo(794f, 482f, 772f, 501f)
                quadTo(750f, 520f, 720f, 520f)
                lineTo(640f, 520f)
                quadTo(607f, 520f, 583.5f, 496.5f)
                quadTo(560f, 473f, 560f, 440f)
                lineTo(560f, 400f)
                lineTo(400f, 400f)
                lineTo(400f, 320f)
                quadTo(400f, 287f, 423.5f, 263.5f)
                quadTo(447f, 240f, 480f, 240f)
                lineTo(520f, 240f)
                lineTo(520f, 240f)
                quadTo(520f, 217f, 532.5f, 199.5f)
                quadTo(545f, 182f, 563f, 171f)
                quadTo(543f, 166f, 522.5f, 163f)
                quadTo(502f, 160f, 480f, 160f)
                quadTo(346f, 160f, 253f, 253f)
                quadTo(160f, 346f, 160f, 480f)
                quadTo(160f, 480f, 160f, 480f)
                quadTo(160f, 480f, 160f, 480f)
                lineTo(360f, 480f)
                quadTo(426f, 480f, 473f, 527f)
                quadTo(520f, 574f, 520f, 640f)
                lineTo(520f, 680f)
                lineTo(400f, 680f)
                lineTo(400f, 790f)
                quadTo(420f, 795f, 439.5f, 797.5f)
                quadTo(459f, 800f, 480f, 800f)
                close()
            }
        }.build()

        return _Globe24Px!!
    }

@Suppress("ObjectPropertyName")
private var _Globe24Px: ImageVector? = null

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
        Icon(imageVector = Globe24Px, contentDescription = null)
    }
}
