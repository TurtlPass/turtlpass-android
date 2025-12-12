package com.turtlpass.biometric.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.turtlpass.ui.icons.Backspace24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.typography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PinKey(
    modifier: Modifier = Modifier,
    pinKeyType: PinKeyType = PinKeyType.None,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .requiredSize(72.dp)
            .clip(shape = RoundedCornerShape(100))
            .then(
                if (onClick != null) {
                    Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(),
                            onClick = onClick
                        )
                } else Modifier
            ),
        contentAlignment = Alignment.Center,
    ) {
        when (pinKeyType) {
            is PinKeyType.Text -> {
                Text(
                    text = pinKeyType.text,
                    style = typography.h2.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    modifier = Modifier.padding(0.dp)
                )
            }

            is PinKeyType.Vector -> {
                Icon(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .padding(end = 3.dp),
                    imageVector = pinKeyType.imageVector,
                    contentDescription = null,
                    tint = colors.text.title,
//                    tint = colors.text.body,
                )
            }

            is PinKeyType.Fingerprint -> {
                Icon(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(32.dp),
                    imageVector = pinKeyType.imageVector,
                    contentDescription = null,
                    tint = colors.text.title,
//                    tint = colors.default.accent,
                )
            }

            PinKeyType.None -> {
                // do nothing
            }
        }
    }
}

private class PinKeyTypeProvider : PreviewParameterProvider<PinKeyType> {
    override val values = sequenceOf(
        PinKeyType.None,
        PinKeyType.Text("0"),
        PinKeyType.Vector(Backspace24Px),
    )
}

@OptIn(ExperimentalMaterialApi::class)
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
