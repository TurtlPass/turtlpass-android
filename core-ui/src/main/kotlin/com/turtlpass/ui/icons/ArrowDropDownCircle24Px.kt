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

val ArrowDropDownCircle24Px: ImageVector
    get() {
        if (_ArrowDropDownCircle24Px != null) {
            return _ArrowDropDownCircle24Px!!
        }
        _ArrowDropDownCircle24Px = ImageVector.Builder(
            name = "ArrowDropDownCircle24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(466f, 586f)
                quadTo(472f, 592f, 480f, 592f)
                quadTo(488f, 592f, 494f, 586f)
                lineTo(606f, 474f)
                quadTo(616f, 464f, 611f, 452f)
                quadTo(606f, 440f, 592f, 440f)
                lineTo(368f, 440f)
                quadTo(354f, 440f, 349f, 452f)
                quadTo(344f, 464f, 354f, 474f)
                lineTo(466f, 586f)
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
                quadTo(614f, 800f, 707f, 707f)
                quadTo(800f, 614f, 800f, 480f)
                quadTo(800f, 346f, 707f, 253f)
                quadTo(614f, 160f, 480f, 160f)
                quadTo(346f, 160f, 253f, 253f)
                quadTo(160f, 346f, 160f, 480f)
                quadTo(160f, 614f, 253f, 707f)
                quadTo(346f, 800f, 480f, 800f)
                close()
                moveTo(480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                close()
            }
        }.build()

        return _ArrowDropDownCircle24Px!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowDropDownCircle24Px: ImageVector? = null

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
        Icon(imageVector = ArrowDropDownCircle24Px, contentDescription = null)
    }
}
