package com.turtlpass.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.turtlpass.R

data class AppTypography(
    val logo: TextStyle,
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val title: TextStyle,
    val subtitle: TextStyle,
    val body: TextStyle,
    val buttonPrimary: TextStyle,
    val buttonSecondary: TextStyle,
)

val defaultFontFamily = FontFamily(
    Font(R.font.sans_light, FontWeight.Light),
    Font(R.font.sans_regular, FontWeight.Normal),
    Font(R.font.sans_semi_bold, FontWeight.SemiBold),
    Font(R.font.sans_bold, FontWeight.Bold),
)

fun appTypography(
    colors: AppColors,
    fontFamily: FontFamily = defaultFontFamily
) = AppTypography(
    logo = TextStyle(
        fontSize = 30.sp,
        lineHeight = 36.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        color = colors.text.primary,
    ),
    h1 = TextStyle(
        fontSize = 39.sp,
        lineHeight = 48.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        color = colors.text.logo,
    ),
    h2 = TextStyle(
        fontSize = 26.sp,
        lineHeight = 30.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Light,
        color = colors.text.title,
    ),
    h3 = TextStyle(
        fontSize = 20.sp,
        lineHeight = 26.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Light,
        color = colors.text.title,
    ),
    title = TextStyle(
        fontSize = 16.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Light,
        lineHeight = 20.sp,
        color = colors.text.body,
    ),
    subtitle = TextStyle(
        fontSize = 12.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Light,
        lineHeight = 15.sp,
        color = colors.text.body,
    ),
    body = TextStyle(
        fontSize = 15.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 21.sp,
        color = colors.text.body,
    ),
    buttonPrimary = TextStyle(
        fontSize = 18.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp,
        color = colors.default.buttonText,
    ),
    buttonSecondary = TextStyle(
        fontSize = 16.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 20.sp,
        color = colors.text.secondaryButton,
    ),
)
