package com.turtlpass.ui.input

import android.content.res.Configuration
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import com.turtlpass.ui.icons.AccountCircle24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.ui.theme.appOutlinedTextFieldColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter

@Composable
fun DropdownField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    focused: Boolean,
    readOnly: Boolean,
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textFieldColors: TextFieldColors = appOutlinedTextFieldColors(),
) {
    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions
            .filter { it is PressInteraction.Release }
            .collect {
                delay(300)
                onClick()
            }
    }

    OutlinedTextField(
        modifier = modifier,
        leadingIcon = leadingIcon,
        label = {
            Text(
                text = label,
                color = if (focused) colors.text.accent else colors.text.body,
                style = typography.title.copy(
                    fontSize = if (value.isNotEmpty() || focused) 14.sp else 16.sp
                ),
            )
        },
        textStyle = typography.title.copy(fontWeight = FontWeight.Normal),
        value = value,
        onValueChange = {},
        interactionSource = interactionSource,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(dimensions.cornerRadius),
        colors = textFieldColors
    )
}

private class DropdownFieldProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf("", "App")
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
    backgroundColor = 0xff424242,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Composable
private fun Preview(
    @PreviewParameter(DropdownFieldProvider::class) item: String
) {
    AppTheme {
        Row(modifier = Modifier.padding(dimensions.x16)) {
            DropdownField(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = AccountCircle24Px,
                        contentDescription = null,
                    )
                },
                label = "Label",
                value = item,
                onClick = {},
                focused = false,
                readOnly = false,
            )
        }
    }
}
