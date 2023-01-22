package com.turtlpass.module.usb

import android.hardware.usb.UsbDevice

sealed class UsbAction {
    object PermissionGranted : UsbAction()
    object PermissionNotGranted : UsbAction()
    data class DeviceAttached(val usbDevice: UsbDevice) : UsbAction()
    object DeviceDetached : UsbAction()
}
