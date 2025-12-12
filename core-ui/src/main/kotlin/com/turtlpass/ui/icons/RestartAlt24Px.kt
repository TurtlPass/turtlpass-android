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

val RestartAlt24Px: ImageVector
    get() {
        if (_RestartAlt24Px != null) {
            return _RestartAlt24Px!!
        }
        _RestartAlt24Px = ImageVector.Builder(
            name = "RestartAlt24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(393f, 828f)
                quadTo(290f, 799f, 225f, 714.5f)
                quadTo(160f, 630f, 160f, 520f)
                quadTo(160f, 463f, 179f, 411.5f)
                quadTo(198f, 360f, 233f, 317f)
                quadTo(244f, 305f, 260f, 304.5f)
                quadTo(276f, 304f, 289f, 317f)
                quadTo(300f, 328f, 300.5f, 344f)
                quadTo(301f, 360f, 290f, 374f)
                quadTo(266f, 405f, 253f, 442f)
                quadTo(240f, 479f, 240f, 520f)
                quadTo(240f, 601f, 287.5f, 664.5f)
                quadTo(335f, 728f, 410f, 751f)
                quadTo(423f, 755f, 431.5f, 766f)
                quadTo(440f, 777f, 440f, 790f)
                quadTo(440f, 810f, 426f, 821.5f)
                quadTo(412f, 833f, 393f, 828f)
                close()
                moveTo(567f, 828f)
                quadTo(548f, 833f, 534f, 821f)
                quadTo(520f, 809f, 520f, 789f)
                quadTo(520f, 777f, 528.5f, 766f)
                quadTo(537f, 755f, 550f, 751f)
                quadTo(625f, 727f, 672.5f, 664f)
                quadTo(720f, 601f, 720f, 520f)
                quadTo(720f, 420f, 650f, 350f)
                quadTo(580f, 280f, 480f, 280f)
                lineTo(477f, 280f)
                lineTo(493f, 296f)
                quadTo(504f, 307f, 504f, 324f)
                quadTo(504f, 341f, 493f, 352f)
                quadTo(482f, 363f, 465f, 363f)
                quadTo(448f, 363f, 437f, 352f)
                lineTo(353f, 268f)
                quadTo(347f, 262f, 344.5f, 255f)
                quadTo(342f, 248f, 342f, 240f)
                quadTo(342f, 232f, 344.5f, 225f)
                quadTo(347f, 218f, 353f, 212f)
                lineTo(437f, 128f)
                quadTo(448f, 117f, 465f, 117f)
                quadTo(482f, 117f, 493f, 128f)
                quadTo(504f, 139f, 504f, 156f)
                quadTo(504f, 173f, 493f, 184f)
                lineTo(477f, 200f)
                lineTo(480f, 200f)
                quadTo(614f, 200f, 707f, 293f)
                quadTo(800f, 386f, 800f, 520f)
                quadTo(800f, 629f, 735f, 714f)
                quadTo(670f, 799f, 567f, 828f)
                close()
            }
        }.build()

        return _RestartAlt24Px!!
    }

@Suppress("ObjectPropertyName")
private var _RestartAlt24Px: ImageVector? = null

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
        Icon(imageVector = RestartAlt24Px, contentDescription = null)
    }
}
