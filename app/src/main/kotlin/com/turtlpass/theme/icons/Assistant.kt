package com.turtlpass.theme.icons

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.turtlpass.theme.AppTheme

public val Icons.Rounded.Assistant: ImageVector
    get() {
        if (_assistant != null) {
            return _assistant!!
        }
        _assistant = materialIcon(name = "Rounded.Assistant") {
            materialPath {
                moveTo(19.0f, 2.0f)
                lineTo(5.0f, 2.0f)
                curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
                verticalLineToRelative(14.0f)
                curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
                horizontalLineToRelative(4.0f)
                lineToRelative(2.29f, 2.29f)
                curveToRelative(0.39f, 0.39f, 1.02f, 0.39f, 1.41f, 0.0f)
                lineTo(15.0f, 20.0f)
                horizontalLineToRelative(4.0f)
                curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
                lineTo(21.0f, 4.0f)
                curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                close()
                moveTo(13.88f, 12.88f)
                lineTo(12.0f, 17.0f)
                lineToRelative(-1.88f, -4.12f)
                lineTo(6.0f, 11.0f)
                lineToRelative(4.12f, -1.88f)
                lineTo(12.0f, 5.0f)
                lineToRelative(1.88f, 4.12f)
                lineTo(18.0f, 11.0f)
                lineToRelative(-4.12f, 1.88f)
                close()
            }
        }
        return _assistant!!
    }

private var _assistant: ImageVector? = null

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
        Icon(imageVector = Icons.Rounded.Assistant, contentDescription = null)
    }
}
