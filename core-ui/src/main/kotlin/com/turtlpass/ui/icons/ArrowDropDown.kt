package com.turtlpass.ui.icons

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.turtlpass.ui.theme.AppTheme

public val Icons.Rounded.ArrowDropDown: ImageVector
    get() {
        if (_arrowDropDown != null) {
            return _arrowDropDown!!
        }
        _arrowDropDown = materialIcon(name = "Rounded.ArrowDropDown") {
            materialPath {
                moveTo(8.71f, 11.71f)
                lineToRelative(2.59f, 2.59f)
                curveToRelative(0.39f, 0.39f, 1.02f, 0.39f, 1.41f, 0.0f)
                lineToRelative(2.59f, -2.59f)
                curveToRelative(0.63f, -0.63f, 0.18f, -1.71f, -0.71f, -1.71f)
                horizontalLineTo(9.41f)
                curveToRelative(-0.89f, 0.0f, -1.33f, 1.08f, -0.7f, 1.71f)
                close()
            }
        }
        return _arrowDropDown!!
    }

private var _arrowDropDown: ImageVector? = null

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
        Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
    }
}
