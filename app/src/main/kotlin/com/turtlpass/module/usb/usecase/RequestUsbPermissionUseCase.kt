package com.turtlpass.module.usb.usecase

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import javax.inject.Inject

class RequestUsbPermissionUseCase @Inject constructor(
    private val context: Context,
    private val usbManager: UsbManager
) {
    operator fun invoke(usbDevice: UsbDevice) {
        PendingIntent.getBroadcast(
            context,
            0,
            Intent(UsbUpdatesUseCase.ACTION_USB_PERMISSION),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
        ).let { intent ->
            usbManager.requestPermission(usbDevice, intent)
        }
    }
}
