package com.turtlpass.module.pin.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.turtlpass.module.pin.PinKeyType
import com.turtlpass.theme.AppRipple
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.typography
import com.turtlpass.theme.icons.Backspace

@ExperimentalMaterialApi
@Composable
fun PinKey(
    modifier: Modifier = Modifier,
    pinKeyType: PinKeyType = PinKeyType.None,
    onClick: (() -> Unit)? = null,
) {
    CompositionLocalProvider(
        LocalRippleTheme provides AppRipple
    ) {
        val indication = rememberRipple()
        Box(
            modifier = modifier
                .requiredSize(72.dp)
                .clip(shape = RoundedCornerShape(100))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = if (onClick != null) indication else null,
                    onClick = { onClick?.invoke() },
                ),
            contentAlignment = Alignment.Center,
        ) {
            when (pinKeyType) {
                is PinKeyType.Text -> {
                    Text(
                        text = pinKeyType.text,
                        style = typography.h2,
                        modifier = Modifier.padding(0.dp)
                    )
                }
                is PinKeyType.Vector -> {
                    Icon(
                        modifier = Modifier
                            .padding(end = 3.dp),
                        imageVector = pinKeyType.imageVector,
                        contentDescription = null,
                        tint = colors.text.body,
                    )
                }
                PinKeyType.None -> {
                    // do nothing
                }
            }
        }
    }
}

private class PinKeyTypeProvider : PreviewParameterProvider<PinKeyType> {
    override val values = sequenceOf(
        PinKeyType.None,
        PinKeyType.Text("0"),
        PinKeyType.Vector(Icons.Rounded.Backspace),
    )
}

@ExperimentalMaterialApi
@Preview(
    name = "Light theme",
    showBackground = true,
    backgroundColor = 0xffffffff,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Preview(
    name = "Dark theme",
    showBackground = true,
    backgroundColor = 0xff424242,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Composable
private fun Preview(
    @PreviewParameter(PinKeyTypeProvider::class) item: PinKeyType
) {
    AppTheme {
        PinKey(
            pinKeyType = item,
            onClick = {},
        )
    }
}
