package com.turtlpass.usb.model

import android.hardware.usb.UsbDevice

data class UsbUiState(
    var usbDeviceUiState: UsbDeviceUiState = UsbDeviceUiState.Detached,
    val isUsbConnected: Boolean = false,
    val usbPermission: UsbPermission = UsbPermission.NotGranted,
    val usbDevice: UsbDevice? = null,
)
