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

val AdminPanelSettings24Px: ImageVector
    get() {
        if (_AdminPanelSettings24Px != null) {
            return _AdminPanelSettings24Px!!
        }
        _AdminPanelSettings24Px = ImageVector.Builder(
            name = "AdminPanelSettings24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(680f, 680f)
                quadTo(705f, 680f, 722.5f, 662.5f)
                quadTo(740f, 645f, 740f, 620f)
                quadTo(740f, 595f, 722.5f, 577.5f)
                quadTo(705f, 560f, 680f, 560f)
                quadTo(655f, 560f, 637.5f, 577.5f)
                quadTo(620f, 595f, 620f, 620f)
                quadTo(620f, 645f, 637.5f, 662.5f)
                quadTo(655f, 680f, 680f, 680f)
                close()
                moveTo(680f, 800f)
                quadTo(711f, 800f, 737f, 785.5f)
                quadTo(763f, 771f, 779f, 747f)
                quadTo(757f, 734f, 732f, 727f)
                quadTo(707f, 720f, 680f, 720f)
                quadTo(653f, 720f, 628f, 727f)
                quadTo(603f, 734f, 581f, 747f)
                quadTo(597f, 771f, 623f, 785.5f)
                quadTo(649f, 800f, 680f, 800f)
                close()
                moveTo(480f, 880f)
                quadTo(341f, 845f, 250.5f, 720.5f)
                quadTo(160f, 596f, 160f, 444f)
                lineTo(160f, 200f)
                lineTo(480f, 80f)
                lineTo(800f, 200f)
                lineTo(800f, 427f)
                quadTo(781f, 419f, 761f, 412.5f)
                quadTo(741f, 406f, 720f, 403f)
                lineTo(720f, 256f)
                lineTo(480f, 166f)
                lineTo(240f, 256f)
                lineTo(240f, 444f)
                quadTo(240f, 491f, 252.5f, 538f)
                quadTo(265f, 585f, 287.5f, 627.5f)
                quadTo(310f, 670f, 342f, 706f)
                quadTo(374f, 742f, 413f, 766f)
                lineTo(413f, 766f)
                quadTo(424f, 798f, 442f, 827f)
                quadTo(460f, 856f, 483f, 879f)
                quadTo(482f, 879f, 481.5f, 879.5f)
                quadTo(481f, 880f, 480f, 880f)
                close()
                moveTo(680f, 880f)
                quadTo(597f, 880f, 538.5f, 821.5f)
                quadTo(480f, 763f, 480f, 680f)
                quadTo(480f, 597f, 538.5f, 538.5f)
                quadTo(597f, 480f, 680f, 480f)
                quadTo(763f, 480f, 821.5f, 538.5f)
                quadTo(880f, 597f, 880f, 680f)
                quadTo(880f, 763f, 821.5f, 821.5f)
                quadTo(763f, 880f, 680f, 880f)
                close()
                moveTo(480f, 466f)
                lineTo(480f, 466f)
                quadTo(480f, 466f, 480f, 466f)
                quadTo(480f, 466f, 480f, 466f)
                quadTo(480f, 466f, 480f, 466f)
                quadTo(480f, 466f, 480f, 466f)
                lineTo(480f, 466f)
                lineTo(480f, 466f)
                lineTo(480f, 466f)
                lineTo(480f, 466f)
                quadTo(480f, 466f, 480f, 466f)
                quadTo(480f, 466f, 480f, 466f)
                quadTo(480f, 466f, 480f, 466f)
                quadTo(480f, 466f, 480f, 466f)
                quadTo(480f, 466f, 480f, 466f)
                quadTo(480f, 466f, 480f, 466f)
                close()
            }
        }.build()

        return _AdminPanelSettings24Px!!
    }

@Suppress("ObjectPropertyName")
private var _AdminPanelSettings24Px: ImageVector? = null

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
        Icon(imageVector = AdminPanelSettings24Px, contentDescription = null)
    }
}
