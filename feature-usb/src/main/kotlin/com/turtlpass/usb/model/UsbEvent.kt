package com.turtlpass.usb.model

import android.hardware.usb.UsbDevice

sealed class UsbEvent {
    data class AttachedWithPermission(val usbDevice: UsbDevice) : UsbEvent()
    data class AttachedWithoutPermission(val usbDevice: UsbDevice) : UsbEvent()
    data class DeviceDetached(val usbDevice: UsbDevice? = null) : UsbEvent()
}
