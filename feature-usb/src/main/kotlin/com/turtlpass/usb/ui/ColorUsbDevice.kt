package com.turtlpass.usb.ui

import androidx.compose.ui.graphics.Color
import com.turtlpass.usb.model.UsbDeviceUiState

fun UsbDeviceUiState.primaryColorUsbDevice() = when (this) {
    UsbDeviceUiState.Attached -> Color(0xff50C878)
    UsbDeviceUiState.Detached -> Color(0xffD22B2B)
    UsbDeviceUiState.AttachedWithoutPermission -> Color(0xffffce00)
}

fun UsbDeviceUiState.secondaryColorUsbDevice() = when (this) {
    UsbDeviceUiState.AttachedWithoutPermission -> Color.Black
    else -> primaryColorUsbDevice()
}
