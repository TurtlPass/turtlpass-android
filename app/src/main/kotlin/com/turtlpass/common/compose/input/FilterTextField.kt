package com.turtlpass.common.compose.input

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import com.turtlpass.R
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography
import com.turtlpass.theme.icons.Cancel

@Composable
fun FilterTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onFocus: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var focused by remember { mutableStateOf(false) }

    TextField(
        modifier = modifier
            .onFocusChanged { focusState ->
                focused = focusState.isFocused
                if (focused) onFocus()
            },
        value = value,
        label = if (value.isNotEmpty() || focused) null else {
            {
                Text(
                    text = stringResource(id = R.string.input_label_filter_app),
                    color = if (focused) colors.text.accent else colors.text.body,
                    style = typography.title.copy(
                        fontSize = if (value.isNotEmpty() || focused) 14.sp else 16.sp
                    ),
                )
            }
        },
        textStyle = typography.title.copy(fontWeight = FontWeight.Normal),
        colors = TextFieldDefaults.textFieldColors(
            textColor = colors.text.input,
            cursorColor = colors.default.accent,
            backgroundColor = colors.default.input,
            disabledLabelColor = colors.default.input,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        onValueChange = { newText ->
            onValueChange(newText.trim())
        },
        shape = RoundedCornerShape(dimensions.cornerRadius),
        singleLine = true,
        trailingIcon = {
            if (value.isNotEmpty() || focused) {
                IconButton(onClick = {
                    onValueChange("")
                    focusManager.clearFocus()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Cancel,
                        contentDescription = null,
                        tint = colors.default.accent,
                    )
                }
            }
        }
    )
}

private class AppNameProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf("", "TurtlPass")
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
    @PreviewParameter(AppNameProvider::class) item: String
) {
    AppTheme {
        FilterTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensions.x16),
            value = item,
            onValueChange = {},
            onFocus = {},
        )
    }
}
