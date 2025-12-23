package com.turtlpass.module.selection.deviceFlow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.turtlpass.module.selection.deviceFlow.DeviceFlowMode
import com.turtlpass.module.selection.topbar.TopAppBarSelection
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.usb.model.UsbUiState
import com.turtlpass.usb.ui.UsbDeviceStateView
import com.turtlpass.usb.ui.rememberStripeBrush

@Composable
fun DeviceFlowContainer(
    flowMode: DeviceFlowMode,
    usbUiState: State<UsbUiState>,
    onUsbRequestPermissionClick: () -> Unit,
    bottomSheetNavigator: BottomSheetNavigator,
    scrimColor: Color,
    onBackClick: (() -> Unit)?,
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
        scrimColor = scrimColor,
    ) {
        when (flowMode) {
            DeviceFlowMode.BottomSheet -> {
                content()
            }

            DeviceFlowMode.FullScreen -> {
                // Add AppBar/Scaffold for full screen
                val statusBarBrush = rememberStripeBrush(
                    state = usbUiState.value.usbDeviceUiState
                )
                Scaffold(
                    topBar = {
                        Column {
                            Column(
                                modifier = Modifier.background(statusBarBrush)
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .windowInsetsTopHeight(WindowInsets.statusBars)
                                )
                                UsbDeviceStateView(
                                    usbDeviceUiState = usbUiState.value.usbDeviceUiState,
                                    onUsbRequestPermissionClick = onUsbRequestPermissionClick
                                )
                            }
                            TopAppBarSelection(
                                title = title(),
                                onBackClick = onBackClick
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
