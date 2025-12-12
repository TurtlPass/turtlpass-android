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

val Encrypted24Px: ImageVector
    get() {
        if (_Encrypted24Px != null) {
            return _Encrypted24Px!!
        }
        _Encrypted24Px = ImageVector.Builder(
            name = "Encrypted24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(420f, 600f)
                lineTo(540f, 600f)
                lineTo(517f, 471f)
                quadTo(537f, 461f, 548.5f, 442f)
                quadTo(560f, 423f, 560f, 400f)
                quadTo(560f, 367f, 536.5f, 343.5f)
                quadTo(513f, 320f, 480f, 320f)
                quadTo(447f, 320f, 423.5f, 343.5f)
                quadTo(400f, 367f, 400f, 400f)
                quadTo(400f, 423f, 411.5f, 442f)
                quadTo(423f, 461f, 443f, 471f)
                lineTo(420f, 600f)
                close()
                moveTo(480f, 880f)
                quadTo(341f, 845f, 250.5f, 720.5f)
                quadTo(160f, 596f, 160f, 444f)
                lineTo(160f, 200f)
                lineTo(480f, 80f)
                lineTo(800f, 200f)
                lineTo(800f, 444f)
                quadTo(800f, 596f, 709.5f, 720.5f)
                quadTo(619f, 845f, 480f, 880f)
                close()
                moveTo(480f, 796f)
                quadTo(584f, 763f, 652f, 664f)
                quadTo(720f, 565f, 720f, 444f)
                lineTo(720f, 255f)
                lineTo(480f, 165f)
                lineTo(240f, 255f)
                lineTo(240f, 444f)
                quadTo(240f, 565f, 308f, 664f)
                quadTo(376f, 763f, 480f, 796f)
                close()
                moveTo(480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                lineTo(480f, 480f)
                lineTo(480f, 480f)
                lineTo(480f, 480f)
                lineTo(480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                close()
            }
        }.build()

        return _Encrypted24Px!!
    }

@Suppress("ObjectPropertyName")
private var _Encrypted24Px: ImageVector? = null

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
        Icon(imageVector = Encrypted24Px, contentDescription = null)
    }
}
