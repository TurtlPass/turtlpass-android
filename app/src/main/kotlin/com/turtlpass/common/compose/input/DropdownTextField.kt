package com.turtlpass.common.compose.input

import android.content.res.Configuration
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography
import com.turtlpass.theme.appOutlinedTextFieldColors
import com.turtlpass.theme.icons.AccountCircle
import com.turtlpass.theme.icons.Close

@Composable
fun DropdownTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    focused: Boolean,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textFieldColors: TextFieldColors = appOutlinedTextFieldColors(),
) {
    val focusManager = LocalFocusManager.current

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
        onValueChange = { newText ->
            onValueChange(newText)
        },
        singleLine = true,
        interactionSource = remember { MutableInteractionSource() },
        readOnly = false,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        trailingIcon = {
            if (value.isEmpty() && focused.not()) {
                trailingIcon?.invoke()
            } else {
                IconButton(onClick = {
                    onValueChange("")
                    focusManager.clearFocus()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                        tint = if (focused) colors.default.accent else colors.default.icon,
                    )
                }
            }
        },
        shape = RoundedCornerShape(dimensions.cornerRadius),
        colors = textFieldColors,
    )
}

private class DropdownTextFieldProvider : PreviewParameterProvider<String> {
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
    @PreviewParameter(DropdownTextFieldProvider::class) item: String
) {
    AppTheme {
        Row(modifier = Modifier.padding(dimensions.x16)) {
            DropdownTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = null,
                    )
                },
                label = "Label",
                value = item,
                focused = false,
            )
        }
    }
}
