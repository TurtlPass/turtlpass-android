package com.turtlpass.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.LocalContentColor
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
object AppRipple : RippleTheme {

    @Composable
    override fun defaultColor() = if (isSystemInDarkTheme()) Color.White else Zomp

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(
        contentColor = LocalContentColor.current,
        lightTheme = isSystemInDarkTheme().not()
    )
}
