package com.turtlpass.theme.icons

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.turtlpass.theme.AppTheme

public val Icons.Rounded.RestartAlt: ImageVector
    get() {
        if (_restartAlt != null) {
            return _restartAlt!!
        }
        _restartAlt = materialIcon(name = "Rounded.RestartAlt") {
            materialPath {
                moveTo(12.0f, 5.0f)
                verticalLineTo(3.21f)
                curveToRelative(0.0f, -0.45f, -0.54f, -0.67f, -0.85f, -0.35f)
                lineTo(8.35f, 5.65f)
                curveToRelative(-0.2f, 0.2f, -0.2f, 0.51f, 0.0f, 0.71f)
                lineToRelative(2.79f, 2.79f)
                curveTo(11.46f, 9.46f, 12.0f, 9.24f, 12.0f, 8.79f)
                verticalLineTo(7.0f)
                curveToRelative(3.31f, 0.0f, 6.0f, 2.69f, 6.0f, 6.0f)
                curveToRelative(0.0f, 2.72f, -1.83f, 5.02f, -4.31f, 5.75f)
                curveTo(13.27f, 18.87f, 13.0f, 19.27f, 13.0f, 19.7f)
                verticalLineToRelative(0.0f)
                curveToRelative(0.0f, 0.65f, 0.62f, 1.16f, 1.25f, 0.97f)
                curveTo(17.57f, 19.7f, 20.0f, 16.64f, 20.0f, 13.0f)
                curveTo(20.0f, 8.58f, 16.42f, 5.0f, 12.0f, 5.0f)
                close()
            }
            materialPath {
                moveTo(6.0f, 13.0f)
                curveToRelative(0.0f, -1.34f, 0.44f, -2.58f, 1.19f, -3.59f)
                curveToRelative(0.3f, -0.4f, 0.26f, -0.95f, -0.09f, -1.31f)
                lineToRelative(0.0f, 0.0f)
                curveTo(6.68f, 7.68f, 5.96f, 7.72f, 5.6f, 8.2f)
                curveTo(4.6f, 9.54f, 4.0f, 11.2f, 4.0f, 13.0f)
                curveToRelative(0.0f, 3.64f, 2.43f, 6.7f, 5.75f, 7.67f)
                curveTo(10.38f, 20.86f, 11.0f, 20.35f, 11.0f, 19.7f)
                verticalLineToRelative(0.0f)
                curveToRelative(0.0f, -0.43f, -0.27f, -0.83f, -0.69f, -0.95f)
                curveTo(7.83f, 18.02f, 6.0f, 15.72f, 6.0f, 13.0f)
                close()
            }
        }
        return _restartAlt!!
    }

private var _restartAlt: ImageVector? = null

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
        Icon(imageVector = Icons.Rounded.RestartAlt, contentDescription = null)
    }
}
