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

val DeleteForever24Px: ImageVector
    get() {
        if (_DeleteForever24Px != null) {
            return _DeleteForever24Px!!
        }
        _DeleteForever24Px = ImageVector.Builder(
            name = "DeleteForever24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(280f, 840f)
                quadTo(247f, 840f, 223.5f, 816.5f)
                quadTo(200f, 793f, 200f, 760f)
                lineTo(200f, 240f)
                lineTo(200f, 240f)
                quadTo(183f, 240f, 171.5f, 228.5f)
                quadTo(160f, 217f, 160f, 200f)
                quadTo(160f, 183f, 171.5f, 171.5f)
                quadTo(183f, 160f, 200f, 160f)
                lineTo(360f, 160f)
                lineTo(360f, 160f)
                quadTo(360f, 143f, 371.5f, 131.5f)
                quadTo(383f, 120f, 400f, 120f)
                lineTo(560f, 120f)
                quadTo(577f, 120f, 588.5f, 131.5f)
                quadTo(600f, 143f, 600f, 160f)
                lineTo(600f, 160f)
                lineTo(760f, 160f)
                quadTo(777f, 160f, 788.5f, 171.5f)
                quadTo(800f, 183f, 800f, 200f)
                quadTo(800f, 217f, 788.5f, 228.5f)
                quadTo(777f, 240f, 760f, 240f)
                lineTo(760f, 240f)
                lineTo(760f, 760f)
                quadTo(760f, 793f, 736.5f, 816.5f)
                quadTo(713f, 840f, 680f, 840f)
                lineTo(280f, 840f)
                close()
                moveTo(480f, 556f)
                lineTo(556f, 632f)
                quadTo(567f, 643f, 584f, 643f)
                quadTo(601f, 643f, 612f, 632f)
                quadTo(623f, 621f, 623f, 604f)
                quadTo(623f, 587f, 612f, 576f)
                lineTo(536f, 500f)
                lineTo(612f, 424f)
                quadTo(623f, 413f, 623f, 396f)
                quadTo(623f, 379f, 612f, 368f)
                quadTo(601f, 357f, 584f, 357f)
                quadTo(567f, 357f, 556f, 368f)
                lineTo(480f, 444f)
                lineTo(404f, 368f)
                quadTo(393f, 357f, 376f, 357f)
                quadTo(359f, 357f, 348f, 368f)
                quadTo(337f, 379f, 337f, 396f)
                quadTo(337f, 413f, 348f, 424f)
                lineTo(424f, 500f)
                lineTo(348f, 576f)
                quadTo(337f, 587f, 337f, 604f)
                quadTo(337f, 621f, 348f, 632f)
                quadTo(359f, 643f, 376f, 643f)
                quadTo(393f, 643f, 404f, 632f)
                lineTo(480f, 556f)
                close()
            }
        }.build()

        return _DeleteForever24Px!!
    }

@Suppress("ObjectPropertyName")
private var _DeleteForever24Px: ImageVector? = null

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
        Icon(imageVector = DeleteForever24Px, contentDescription = null)
    }
}
