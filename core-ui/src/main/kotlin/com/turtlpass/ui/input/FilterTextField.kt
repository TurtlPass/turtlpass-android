package com.turtlpass.ui.input

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import com.turtlpass.ui.icons.Cancel24Px
import com.turtlpass.ui.icons.Search24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography

@Composable
fun FilterTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var focused by remember { mutableStateOf(false) }

    TextField(
        modifier = modifier
            .onFocusChanged { focusState ->
                focused = focusState.isFocused
                onFocusChanged(focused)
            },
        enabled = enabled,
        value = value,
        label = if (value.isNotEmpty() || focused) null else {
            {
                Text(
                    text = label,
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
        leadingIcon = {
            Icon(
                imageVector = Search24Px,
                tint = colors.text.title,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (value.isNotEmpty() || focused) {
                IconButton(onClick = {
                    onValueChange("")
                    focusManager.clearFocus()
                }) {
                    Icon(
                        imageVector = Cancel24Px,
                        contentDescription = null,
                        tint = colors.default.accent,
                    )
                }
            }
        }
    )
}

private class FilterTextFieldProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf("", "TurtlPass")
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
//@Preview(
//    name = "Dark theme",
//    showBackground = true,
//    backgroundColor = 0xff424242,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showSystemUi = false,
//    device = Devices.PIXEL_XL,
//)
@Composable
private fun Preview(
    @PreviewParameter(FilterTextFieldProvider::class) item: String
) {
    AppTheme {
        FilterTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensions.x16),
            label = "Type to filter listed apps",
            value = item,
            onValueChange = {},
            onFocusChanged = {},
        )
    }
}
