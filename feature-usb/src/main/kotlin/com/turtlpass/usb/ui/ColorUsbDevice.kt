package com.turtlpass.usb.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.turtlpass.usb.model.UsbDeviceUiState

@Composable
fun colorUsbDevice(usbDeviceUiState: UsbDeviceUiState) = when (usbDeviceUiState) {
    UsbDeviceUiState.Attached -> Color(0xff50C878)
    UsbDeviceUiState.Detached -> Color(0xffD22B2B)
    UsbDeviceUiState.MissingPermission -> Color(0xffFFBF00)
}
