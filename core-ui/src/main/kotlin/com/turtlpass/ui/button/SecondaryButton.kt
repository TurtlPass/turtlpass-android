package com.turtlpass.ui.button

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.ui.theme.appRipple
import com.turtlpass.ui.theme.disabledColor

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.appRipple(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colors.default.input,
            disabledBackgroundColor = colors.default.input.disabledColor(false)
        ),
        border = null,
        shape = RoundedCornerShape(size = dimensions.x32),
        enabled = enabled,
        onClick = onClick,
        elevation = null
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = typography.buttonSecondary.copy(
                color = colors.text.secondaryButton.disabledColor(enabled)
            )
        )
    }
}

private class SecondaryButtonSmallProvider : PreviewParameterProvider<Boolean> {
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
    @PreviewParameter(SecondaryButtonSmallProvider::class) item: Boolean
) {
    AppTheme {
        Row(modifier = Modifier.padding(dimensions.x8)) {
            SecondaryButton(
                text = "Cancel",
                enabled = item,
                onClick = {}
            )
        }
    }
}
