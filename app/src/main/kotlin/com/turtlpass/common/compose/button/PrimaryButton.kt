package com.turtlpass.common.compose.button

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.turtlpass.theme.AppRipple
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography
import com.turtlpass.theme.disabledColor
import com.turtlpass.theme.icons.Security

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Rounded.Security,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    backgroundColor: Color = colors.default.button,
) {
    val paddingStart = dimensions.x8

    CompositionLocalProvider(
        LocalRippleTheme provides AppRipple
    ) {
        Button(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(size = dimensions.x32),
            border = null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor,
                disabledBackgroundColor = backgroundColor.disabledColor(false)
            ),
            contentPadding = PaddingValues(
                top = dimensions.x16,
                bottom = dimensions.x16,
                start = dimensions.x16 + paddingStart,
                end = dimensions.x32,
            ),
            content = {
                Icon(
                    imageVector = imageVector,
                    modifier = Modifier
                        .size(dimensions.iconSize),
                    contentDescription = text,
                    tint = Color.White
                )
                Text(
                    modifier = Modifier
                        .padding(start = paddingStart),
                    text = text,
                    textAlign = TextAlign.Center,
                    style = typography.buttonPrimary.copy(
                        color = colors.default.buttonText.disabledColor(enabled)
                    )
                )
            }
        )
    }
}

private class PrimaryButtonProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(true, false)
}

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
    backgroundColor = 0xff303030,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Composable
private fun Preview(
    @PreviewParameter(PrimaryButtonProvider::class) item: Boolean
) {
    AppTheme {
        Row(modifier = Modifier.padding(dimensions.x8)) {
            PrimaryButton(
                text = "Unlock",
                enabled = item,
                onClick = {},
                backgroundColor = colors.default.button
            )
        }
    }
}
