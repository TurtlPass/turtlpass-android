package com.turtlpass.usb.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import com.turtlpass.usb.di.UsbPermissionReceiverEntryPoint
import com.turtlpass.usb.model.UsbEvent
import com.turtlpass.usb.usecase.UsbUpdatesUseCase
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.runBlocking
import timber.log.Timber

/**
 * Handles USB permission results coming from UsbManager.requestPermission().
 *
 * Must be declared explicitly in AndroidManifest.xml.
 */
class UsbPermissionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_USB_PERMISSION) return

        val deviceId = intent.getIntExtra(EXTRA_DEVICE_ID, -1)
        if (deviceId == -1) {
            Timber.w("USB permission result without deviceId")
            return
        }
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val device = usbManager.deviceList.values
            .firstOrNull { it.deviceId == deviceId }

        if (device == null) {
            Timber.w("USB permission result but device missing")
            return
        }

        // Access DI via EntryPoint
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            UsbPermissionReceiverEntryPoint::class.java
        ).apply {
            requestUsbPermissionUseCase().onPermissionResult(deviceId)
        }

        runBlocking {
            // check hasPermission because EXTRA_PERMISSION_GRANTED is unreliable
            if (usbManager.hasPermission(device)) {
                Timber.i("USB permission granted for ${device.deviceName}")
                UsbUpdatesUseCase.emit(UsbEvent.AttachedWithPermission(device))
            } else {
                Timber.w("USB permission denied for ${device.deviceName}")
                UsbUpdatesUseCase.emit(UsbEvent.AttachedWithoutPermission(device))
            }
        }
    }

    companion object {
        const val ACTION_USB_PERMISSION = "com.turtlpass.USB_PERMISSION"
        const val EXTRA_DEVICE_ID = "device_id"
    }
}
