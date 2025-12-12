package com.turtlpass.module.selection.deviceFlow

import androidx.compose.material.navigation.bottomSheet
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.deviceDestination(
    mode: DeviceFlowMode,
    route: String,
    content: @Composable () -> Unit
) {
    when (mode) {
        DeviceFlowMode.BottomSheet -> bottomSheet(route) { content() }
        DeviceFlowMode.FullScreen -> composable(route) { content() }
    }
}
