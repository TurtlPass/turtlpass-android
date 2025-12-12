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

val SecurityKey24Px: ImageVector
    get() {
        if (_SecurityKey24Px != null) {
            return _SecurityKey24Px!!
        }
        _SecurityKey24Px = ImageVector.Builder(
            name = "SecurityKey24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(400f, 920f)
                quadTo(367f, 920f, 343.5f, 896.5f)
                quadTo(320f, 873f, 320f, 840f)
                lineTo(320f, 720f)
                lineTo(320f, 720f)
                quadTo(287f, 720f, 263.5f, 696.5f)
                quadTo(240f, 673f, 240f, 640f)
                lineTo(240f, 120f)
                quadTo(240f, 87f, 263.5f, 63.5f)
                quadTo(287f, 40f, 320f, 40f)
                lineTo(640f, 40f)
                quadTo(673f, 40f, 696.5f, 63.5f)
                quadTo(720f, 87f, 720f, 120f)
                lineTo(720f, 640f)
                quadTo(720f, 673f, 696.5f, 696.5f)
                quadTo(673f, 720f, 640f, 720f)
                lineTo(640f, 720f)
                lineTo(640f, 840f)
                quadTo(640f, 873f, 616.5f, 896.5f)
                quadTo(593f, 920f, 560f, 920f)
                lineTo(400f, 920f)
                close()
                moveTo(480f, 500f)
                quadTo(530f, 500f, 565f, 465f)
                quadTo(600f, 430f, 600f, 380f)
                quadTo(600f, 330f, 565f, 295f)
                quadTo(530f, 260f, 480f, 260f)
                quadTo(430f, 260f, 395f, 295f)
                quadTo(360f, 330f, 360f, 380f)
                quadTo(360f, 430f, 395f, 465f)
                quadTo(430f, 500f, 480f, 500f)
                close()
                moveTo(400f, 840f)
                lineTo(560f, 840f)
                quadTo(560f, 840f, 560f, 840f)
                quadTo(560f, 840f, 560f, 840f)
                lineTo(560f, 720f)
                lineTo(400f, 720f)
                lineTo(400f, 840f)
                quadTo(400f, 840f, 400f, 840f)
                quadTo(400f, 840f, 400f, 840f)
                close()
                moveTo(320f, 640f)
                lineTo(640f, 640f)
                quadTo(640f, 640f, 640f, 640f)
                quadTo(640f, 640f, 640f, 640f)
                lineTo(640f, 120f)
                quadTo(640f, 120f, 640f, 120f)
                quadTo(640f, 120f, 640f, 120f)
                lineTo(320f, 120f)
                quadTo(320f, 120f, 320f, 120f)
                quadTo(320f, 120f, 320f, 120f)
                lineTo(320f, 640f)
                quadTo(320f, 640f, 320f, 640f)
                quadTo(320f, 640f, 320f, 640f)
                close()
                moveTo(480f, 420f)
                quadTo(463f, 420f, 451.5f, 408.5f)
                quadTo(440f, 397f, 440f, 380f)
                quadTo(440f, 363f, 451.5f, 351.5f)
                quadTo(463f, 340f, 480f, 340f)
                quadTo(497f, 340f, 508.5f, 351.5f)
                quadTo(520f, 363f, 520f, 380f)
                quadTo(520f, 397f, 508.5f, 408.5f)
                quadTo(497f, 420f, 480f, 420f)
                close()
                moveTo(320f, 640f)
                quadTo(320f, 640f, 320f, 640f)
                quadTo(320f, 640f, 320f, 640f)
                lineTo(320f, 640f)
                quadTo(320f, 640f, 320f, 640f)
                quadTo(320f, 640f, 320f, 640f)
                lineTo(640f, 640f)
                quadTo(640f, 640f, 640f, 640f)
                quadTo(640f, 640f, 640f, 640f)
                lineTo(640f, 640f)
                quadTo(640f, 640f, 640f, 640f)
                quadTo(640f, 640f, 640f, 640f)
                lineTo(320f, 640f)
                close()
            }
        }.build()

        return _SecurityKey24Px!!
    }

@Suppress("ObjectPropertyName")
private var _SecurityKey24Px: ImageVector? = null

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
        Icon(imageVector = SecurityKey24Px, contentDescription = null)
    }
}
