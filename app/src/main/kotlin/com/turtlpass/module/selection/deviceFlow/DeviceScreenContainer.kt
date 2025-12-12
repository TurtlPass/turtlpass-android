package com.turtlpass.module.selection.deviceFlow

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.turtlpass.usb.model.UsbUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceScreenContainer(
    mode: DeviceFlowMode,
    usbUiState: State<UsbUiState>,
    title: String? = null,
    onCancel: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    when (mode) {
        DeviceFlowMode.BottomSheet -> {
            BottomSheetContainer(
                usbUiState = usbUiState,
                title = title,
                onCancel = onCancel,
            ) {
                content()
            }
        }

        DeviceFlowMode.FullScreen ->
            content() // handled by DeviceFlowContainer Scaffold
    }
}
