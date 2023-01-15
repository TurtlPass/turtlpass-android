package com.turtlpass.module.chooser

import android.hardware.usb.UsbDevice

data class UsbState(
    val usbPermission: UsbPermission = UsbPermission.NotGranted,
    val usbDevice: UsbDevice? = null,
    val usbWriteResult: UsbWriteResult? = null,
)

enum class UsbWriteResult {
    Success, Error
}

enum class UsbPermission {
    Granted, NotGranted
}
