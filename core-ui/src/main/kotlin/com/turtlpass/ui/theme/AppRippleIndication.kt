package com.turtlpass.ui.theme

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.appRipple(bounded: Boolean = true, color: Color? = null): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isDarkTheme = isSystemInDarkTheme()
    val resolvedColor = color ?: if (isDarkTheme) Color.White else Zomp
    val indication = ripple(color = { resolvedColor }, bounded = bounded)
    return this.indication(interactionSource = interactionSource, indication = indication)
}
