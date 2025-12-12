package com.turtlpass.usb.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.turtlpass.domain.parcelable
import com.turtlpass.usb.di.UsbPermissionReceiverEntryPoint
import com.turtlpass.usb.model.UsbAction
import com.turtlpass.usb.usecase.UsbUpdatesUseCase
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * Handles USB permission results coming from UsbManager.requestPermission().
 *
 * Must be declared explicitly in AndroidManifest.xml.
 */
class UsbPermissionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_USB_PERMISSION) return

        val device: UsbDevice? = intent.parcelable(UsbManager.EXTRA_DEVICE)
        val granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)

        if (device == null) {
            Timber.w("UsbPermissionReceiver: Missing device in intent")
            return
        }

        // Access DI via EntryPoint
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            UsbPermissionReceiverEntryPoint::class.java
        ).apply {
            requestUsbPermissionUseCase().onPermissionResult(device.deviceId)
        }

        CoroutineScope(Dispatchers.Default).launch {
            if (granted) {
                Timber.i("USB permission granted for ${device.deviceName}")
                UsbUpdatesUseCase.emit(UsbAction.PermissionGranted(device))
            } else {
                Timber.w("USB permission denied for ${device.deviceName}")
                UsbUpdatesUseCase.emit(UsbAction.PermissionNotGranted(device))
            }
        }
    }

    companion object {
        const val ACTION_USB_PERMISSION = "com.turtlpass.USB_PERMISSION"
    }
}
