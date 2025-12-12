package com.turtlpass.ui.button

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turtlpass.ui.icons.Key24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.ui.theme.appRipple
import com.turtlpass.ui.theme.disabledColor

@Composable
fun PrimaryButtonSmall(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Key24Px,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    backgroundColor: Color = colors.default.button,
) {
    val paddingStart = dimensions.x8

    Button(
        modifier = modifier.appRipple(),
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
            top = dimensions.x8 + dimensions.x4,
            bottom = dimensions.x8 + dimensions.x4,
            start = dimensions.x8 + paddingStart,
            end = dimensions.x16 + dimensions.x8,
        ),
        content = {
            Icon(
                imageVector = imageVector,
                modifier = Modifier
                    .size(18.dp),
//                    .size(dimensions.iconSize),
                contentDescription = text,
                tint = Color.White
            )
            Text(
                modifier = Modifier
                    .padding(start = paddingStart),
                text = text,
                textAlign = TextAlign.Start,
                style = typography.buttonPrimary.copy(
                    color = colors.default.buttonText.disabledColor(enabled),
                    fontSize = 14.sp,
                )
            )
        }
    )
}

private class PrimaryButtonSmallProvider : PreviewParameterProvider<Boolean> {
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
    @PreviewParameter(PrimaryButtonSmallProvider::class) item: Boolean
) {
    AppTheme {
        Row(modifier = Modifier.padding(dimensions.x8)) {
            PrimaryButtonSmall(
                text = "Unlock",
                enabled = item,
                onClick = {},
                backgroundColor = colors.default.button
            )
        }
    }
}
