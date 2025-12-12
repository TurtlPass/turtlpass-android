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

val MoreVert24Px: ImageVector
    get() {
        if (_MoreVert24Px != null) {
            return _MoreVert24Px!!
        }
        _MoreVert24Px = ImageVector.Builder(
            name = "MoreVert24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(480f, 800f)
                quadTo(447f, 800f, 423.5f, 776.5f)
                quadTo(400f, 753f, 400f, 720f)
                quadTo(400f, 687f, 423.5f, 663.5f)
                quadTo(447f, 640f, 480f, 640f)
                quadTo(513f, 640f, 536.5f, 663.5f)
                quadTo(560f, 687f, 560f, 720f)
                quadTo(560f, 753f, 536.5f, 776.5f)
                quadTo(513f, 800f, 480f, 800f)
                close()
                moveTo(480f, 560f)
                quadTo(447f, 560f, 423.5f, 536.5f)
                quadTo(400f, 513f, 400f, 480f)
                quadTo(400f, 447f, 423.5f, 423.5f)
                quadTo(447f, 400f, 480f, 400f)
                quadTo(513f, 400f, 536.5f, 423.5f)
                quadTo(560f, 447f, 560f, 480f)
                quadTo(560f, 513f, 536.5f, 536.5f)
                quadTo(513f, 560f, 480f, 560f)
                close()
                moveTo(480f, 320f)
                quadTo(447f, 320f, 423.5f, 296.5f)
                quadTo(400f, 273f, 400f, 240f)
                quadTo(400f, 207f, 423.5f, 183.5f)
                quadTo(447f, 160f, 480f, 160f)
                quadTo(513f, 160f, 536.5f, 183.5f)
                quadTo(560f, 207f, 560f, 240f)
                quadTo(560f, 273f, 536.5f, 296.5f)
                quadTo(513f, 320f, 480f, 320f)
                close()
            }
        }.build()

        return _MoreVert24Px!!
    }

@Suppress("ObjectPropertyName")
private var _MoreVert24Px: ImageVector? = null

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
        Icon(imageVector = MoreVert24Px, contentDescription = null)
    }
}
