package com.turtlpass.common.compose.input

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography
import com.turtlpass.theme.appOutlinedTextFieldColors
import com.turtlpass.theme.appTextSelectionColors
import com.turtlpass.theme.icons.Visibility
import com.turtlpass.theme.icons.VisibilityOff

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    error: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textFieldColors: TextFieldColors = appOutlinedTextFieldColors(),
) {
    var focused by remember {
        mutableStateOf(false)
    }
    var visible: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    CompositionLocalProvider(
        LocalTextSelectionColors provides appTextSelectionColors()
    ) {
        OutlinedTextField(
            modifier = modifier
                .onFocusChanged { focusState ->
                    focused = focusState.isFocused
                },
            label = {
                Text(
                    text = label,
                    color = colors.text.body,
                    style = typography.title.copy(
                        fontSize = if (value.isNotEmpty() || focused) 14.sp else 16.sp
                    ),
                )
            },
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            isError = error,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            visualTransformation = if (visible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { visible = !visible }) {
                    Crossfade(
                        targetState = visible,
                    ) { visible ->
                        Icon(
                            imageVector = if (visible) {
                                Icons.Outlined.Visibility
                            } else {
                                Icons.Outlined.VisibilityOff
                            },
                            tint = colors.default.icon,
                            contentDescription = null
                        )
                    }
                }
            },
            shape = RoundedCornerShape(dimensions.cornerRadius),
            textStyle = typography.title.copy(fontWeight = FontWeight.Normal),
            colors = textFieldColors,
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPermissionsApi
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
private fun Preview() {
    AppTheme {
        var value by remember {
            mutableStateOf("turtle conservation")
        }
        PasswordTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensions.x16),
            label = "Passphrase",
            value = value,
            onValueChange = { newValue -> value = newValue },
        )
    }
}
