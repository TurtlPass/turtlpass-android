package com.turtlpass.usb.model

import android.hardware.usb.UsbDevice

sealed class UsbAction {
    data class DeviceAttached(val usbDevice: UsbDevice) : UsbAction()
    data class DeviceDetached(val usbDevice: UsbDevice? = null) : UsbAction()
    data class PermissionGranted(val usbDevice: UsbDevice) : UsbAction()
    data class PermissionNotGranted(val usbDevice: UsbDevice) : UsbAction()
}
