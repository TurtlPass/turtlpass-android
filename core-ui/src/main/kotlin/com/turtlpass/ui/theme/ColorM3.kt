package com.turtlpass.ui.theme

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.turtlpass.ui.theme.AppTheme.colors

@Composable
fun appOutlinedTextFieldColorsM3(
    backgroundColor: Color = colors.default.background
): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = backgroundColor,
        focusedContainerColor = backgroundColor,
        focusedBorderColor = colors.default.accent,
        unfocusedBorderColor = colors.default.border,
        cursorColor = colors.default.accent,
        disabledContainerColor = backgroundColor,
        disabledBorderColor = colors.default.border,
        unfocusedTextColor = colors.text.body,
        focusedTextColor = colors.text.body,
        disabledTextColor = colors.text.body,
    )
}
