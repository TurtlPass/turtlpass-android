package com.turtlpass.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppDimensions(
    val iconSize: Dp,
    val cornerRadius: Dp,
    val x2: Dp,
    val x4: Dp,
    val x8: Dp,
    val x16: Dp,
    val x32: Dp,
    val x64: Dp,
)

fun appDimensions() = AppDimensions(
    iconSize = 24.dp,
    cornerRadius = 16.dp,
    x2 = 2.dp,
    x4 = 4.dp,
    x8 = 8.dp,
    x16 = 16.dp,
    x32 = 32.dp,
    x64 = 64.dp,
)
