package com.turtlpass.module.selection.deviceFlow

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.turtlpass.module.selection.topbar.TopAppBarSelection
import com.turtlpass.ui.statusBar.StatusBarBackground
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.usb.model.UsbUiState
import com.turtlpass.usb.ui.UsbDeviceStateView
import com.turtlpass.usb.ui.colorUsbDevice

@Composable
fun DeviceFlowContainer(
    flowMode: DeviceFlowMode,
    usbUiState: State<UsbUiState>,
    bottomSheetNavigator: BottomSheetNavigator,
    finish: () -> Unit,
    title: @Composable () -> String = { "" },
    content: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        modifier = Modifier
            .statusBarsPadding(),
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(
            topStart = dimensions.x16,
            topEnd = dimensions.x16
        ),
        sheetBackgroundColor = colors.default.sheetBackground,
        scrimColor = colors.default.scrim,
    ) {
        when (flowMode) {
            DeviceFlowMode.BottomSheet -> {
                content()
            }

            DeviceFlowMode.FullScreen -> {
                // Add AppBar/Scaffold for full screen
                val statusBarColor = colorUsbDevice(usbUiState.value.usbDeviceUiState)
                val animatedStatusBarColor by animateColorAsState(
                    targetValue = statusBarColor,
                    animationSpec = tween(durationMillis = 500)
                )

                Scaffold(
                    topBar = {
                        Column {
                            StatusBarBackground(color = animatedStatusBarColor)
                            UsbDeviceStateView(
                                usbDeviceUiState = usbUiState.value.usbDeviceUiState,
                                backgroundColor = animatedStatusBarColor
                            )
                            TopAppBarSelection(
                                title = title(),
                                onBackClick = finish
                            )
                        }
                    },
                    backgroundColor = colors.default.background
                ) { padding ->
                    Box(Modifier.padding(padding)) {
                        content()
                    }
                }
            }
        }
    }
}
