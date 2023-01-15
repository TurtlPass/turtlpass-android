package com.turtlpass.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun AppTheme(
    colors: AppColors = appColors(isDarkTheme = isSystemInDarkTheme()),
    typography: AppTypography = appTypography(colors = colors),
    dimensions: AppDimensions = appDimensions(),
    shapes: AppShapes = appShapes(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        LocalAppDimensions provides dimensions,
        LocalAppShapes provides shapes,
    ) {
        MaterialTheme(
            content = content
        )
    }
}

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current

    val dimensions: AppDimensions
        @Composable
        get() = LocalAppDimensions.current

    val shapes: AppShapes
        @Composable
        get() = LocalAppShapes.current
}

private val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("AppColors not provided")
}

private val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("AppTypography not provided")
}

private val LocalAppDimensions = staticCompositionLocalOf<AppDimensions> {
    error("AppDimensions not provided")
}

private val LocalAppShapes = staticCompositionLocalOf<AppShapes> {
    error("AppShapes not provided")
}
