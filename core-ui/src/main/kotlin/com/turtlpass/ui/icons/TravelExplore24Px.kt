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

val TravelExplore24Px: ImageVector
    get() {
        if (_TravelExplore24Px != null) {
            return _TravelExplore24Px!!
        }
        _TravelExplore24Px = ImageVector.Builder(
            name = "TravelExplore24Px",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(80f, 480f)
                quadTo(80f, 397f, 111.5f, 324f)
                quadTo(143f, 251f, 197f, 197f)
                quadTo(251f, 143f, 324f, 111.5f)
                quadTo(397f, 80f, 480f, 80f)
                quadTo(607f, 80f, 706.5f, 150f)
                quadTo(806f, 220f, 851f, 331f)
                quadTo(858f, 348f, 851.5f, 365f)
                quadTo(845f, 382f, 828f, 388f)
                quadTo(812f, 393f, 797.5f, 385f)
                quadTo(783f, 377f, 777f, 361f)
                quadTo(753f, 301f, 708f, 255f)
                quadTo(663f, 209f, 600f, 184f)
                lineTo(600f, 200f)
                quadTo(600f, 233f, 576.5f, 256.5f)
                quadTo(553f, 280f, 520f, 280f)
                lineTo(440f, 280f)
                lineTo(440f, 360f)
                quadTo(440f, 377f, 428.5f, 388.5f)
                quadTo(417f, 400f, 400f, 400f)
                lineTo(320f, 400f)
                lineTo(320f, 480f)
                lineTo(360f, 480f)
                quadTo(377f, 480f, 388.5f, 491.5f)
                quadTo(400f, 503f, 400f, 520f)
                lineTo(400f, 600f)
                lineTo(360f, 600f)
                lineTo(168f, 408f)
                quadTo(165f, 426f, 162.5f, 444f)
                quadTo(160f, 462f, 160f, 480f)
                quadTo(160f, 602f, 240.5f, 693f)
                quadTo(321f, 784f, 443f, 798f)
                quadTo(459f, 800f, 469.5f, 811.5f)
                quadTo(480f, 823f, 480f, 840f)
                quadTo(480f, 857f, 468.5f, 868.5f)
                quadTo(457f, 880f, 441f, 878f)
                quadTo(288f, 863f, 184f, 750f)
                quadTo(80f, 637f, 80f, 480f)
                close()
                moveTo(816f, 832f)
                lineTo(716f, 732f)
                quadTo(695f, 744f, 671f, 752f)
                quadTo(647f, 760f, 620f, 760f)
                quadTo(545f, 760f, 492.5f, 707.5f)
                quadTo(440f, 655f, 440f, 580f)
                quadTo(440f, 505f, 492.5f, 452.5f)
                quadTo(545f, 400f, 620f, 400f)
                quadTo(695f, 400f, 747.5f, 452.5f)
                quadTo(800f, 505f, 800f, 580f)
                quadTo(800f, 607f, 792f, 631f)
                quadTo(784f, 655f, 772f, 676f)
                lineTo(872f, 776f)
                quadTo(883f, 787f, 883f, 804f)
                quadTo(883f, 821f, 872f, 832f)
                quadTo(861f, 843f, 844f, 843f)
                quadTo(827f, 843f, 816f, 832f)
                close()
                moveTo(620f, 680f)
                quadTo(662f, 680f, 691f, 651f)
                quadTo(720f, 622f, 720f, 580f)
                quadTo(720f, 538f, 691f, 509f)
                quadTo(662f, 480f, 620f, 480f)
                quadTo(578f, 480f, 549f, 509f)
                quadTo(520f, 538f, 520f, 580f)
                quadTo(520f, 622f, 549f, 651f)
                quadTo(578f, 680f, 620f, 680f)
                close()
            }
        }.build()

        return _TravelExplore24Px!!
    }

@Suppress("ObjectPropertyName")
private var _TravelExplore24Px: ImageVector? = null

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
        Icon(imageVector = TravelExplore24Px, contentDescription = null)
    }
}
