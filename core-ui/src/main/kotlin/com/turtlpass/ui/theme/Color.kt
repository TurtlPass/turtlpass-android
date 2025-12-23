package com.turtlpass.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.turtlpass.ui.theme.AppTheme.colors
import kotlin.reflect.KProperty

/**
 * Color Palette: https://coolors.co/eff9f3-292624-43aa8f-af7038-afefd9
 */
val Zomp2 = Color(0xFF37C2A3)
val Zomp = Color(0xff43aa8f)
val MagicMint = Color(0xffafefd9)
val MintCream = Color(0xffeff9f3)
val Cooper = Color(0xffaf7038)
val RaisinBlack = Color(0xff292624)
val Blackish = Color(0xff121212)
val DarkBackground = Color(0xff303030)
val DarkCard = Color(0xff424242)
val Grey100 = Color(0xfff3f4f6)
val Grey200 = Color(0xffe2e5e9)
val Grey300 = Color(0xff9ca5af)
val Grey400 = Color(0xff6c757f)
val Grey500 = Color(0xff4d5660)

fun Color.disabledColor(enabled: Boolean): Color {
    return if (enabled) this else copy(alpha = 0.5f)
}

fun appColors(isDarkTheme: Boolean) = AppColors(isDarkTheme)

interface ColorContainer {
    val isDarkTheme: Boolean
}

data class ColorBox(val light: Color, val dark: Color) {
    operator fun getValue(colorContainer: ColorContainer, prop: KProperty<*>): Color {
        return if (colorContainer.isDarkTheme) dark else light
    }
}

data class AppColors(
    override val isDarkTheme: Boolean,
) : ColorContainer {

    val default by lazy { Default() }
    val text by lazy { Text() }

    inner class Default internal constructor() : ColorContainer by this {
        val background by ColorBox(light = Color.White, dark = DarkBackground)
        val sheetBackground by ColorBox(light = Color.White, dark = DarkCard)
        val cardBackground by ColorBox(light = MintCream, dark = RaisinBlack)
        val scrim by ColorBox(light = Color.Black.copy(alpha = 0.4f), Color.Black.copy(alpha = 0.6f))
        val input by ColorBox(light = MintCream, dark = RaisinBlack)
        val unfocused by ColorBox(light = MagicMint, dark = Color.White)
        val accent by ColorBox(light = Zomp, dark = Zomp)
        val button by ColorBox(light = Zomp, dark = Zomp)
        val buttonText by ColorBox(light = Color.White, dark = Color.White)
        val text by ColorBox(light = RaisinBlack, dark = Color.White)
        val placeholder by ColorBox(light = Grey100, dark = Grey400)
        val placeholderHighlight by ColorBox(light = Grey200, dark = Grey200)
        val icon by ColorBox(light = Grey300, dark = Color.White)
        val dragHandle by ColorBox(light = Grey200, dark = Grey200)
        val border by ColorBox(light = Grey200, dark = Grey400)
        val destructive by ColorBox(light = Color.Red.copy(alpha = 0.85f), dark = Color.Red)
        val error by ColorBox(light = Cooper, dark = Cooper)
    }

    inner class Text internal constructor() : ColorContainer by this {
        val primary by ColorBox(light = RaisinBlack, dark = Color.White)
        val input by ColorBox(light = Grey200, dark = Color.White)
        val logo by ColorBox(light = Zomp, dark = Zomp)
        val title by ColorBox(light = Grey500, dark = Color.White)
        val body by ColorBox(light = Grey400, dark = Color.White)
        val secondaryButton by ColorBox(light = Zomp, dark = Zomp)
        val accent by ColorBox(light = Zomp, dark = Zomp)
    }
}

@Composable
fun appOutlinedTextFieldColors(
    backgroundColor: Color = colors.default.background
): TextFieldColors {
    return TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = colors.default.accent,
        unfocusedBorderColor = colors.default.border,
        cursorColor = colors.default.accent,
        backgroundColor = backgroundColor,
    )
}

@Composable
fun appTextSelectionColors() = TextSelectionColors(
    handleColor = colors.default.accent,
    backgroundColor = colors.default.accent.copy(alpha = 0.3f)
)
