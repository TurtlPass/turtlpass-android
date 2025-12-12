package com.turtlpass.usb.usecase

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.turtlpass.usb.receiver.UsbPermissionReceiver
import timber.log.Timber
import javax.inject.Inject

class RequestUsbPermissionUseCase @Inject constructor(
    private val context: Context,
    private val usbManager: UsbManager
) {
    private val pendingPermissionRequests = mutableSetOf<Int>()

    operator fun invoke(usbDevice: UsbDevice) {
        val id = usbDevice.deviceId

        // Prevent duplicate permission request
        if (pendingPermissionRequests.contains(id)) {
            Timber.i("Permission already requested for device $id")
            return
        }

        pendingPermissionRequests.add(id)

        PendingIntent.getBroadcast(
            context,
            id, // unique per device
            Intent(context, UsbPermissionReceiver::class.java).apply {
                action = UsbPermissionReceiver.ACTION_USB_PERMISSION
            },
            PendingIntent.FLAG_IMMUTABLE
        ).let { intent ->
            usbManager.requestPermission(usbDevice, intent)
        }
    }

    fun onPermissionResult(deviceId: Int) {
        pendingPermissionRequests.remove(deviceId)
    }
}
