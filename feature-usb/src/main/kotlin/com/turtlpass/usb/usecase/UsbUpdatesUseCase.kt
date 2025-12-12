package com.turtlpass.usb.usecase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.turtlpass.di.ApplicationScope
import com.turtlpass.domain.parcelable
import com.turtlpass.usb.hardware.HardwareConfiguration
import com.turtlpass.usb.model.UsbAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class UsbUpdatesUseCase @Inject constructor(
    @param:ApplicationScope private val coroutineScope: CoroutineScope,
    private val context: Context,
    private val usbManager: UsbManager,
    private val requestUsbPermissionUseCase: RequestUsbPermissionUseCase,
) {
    suspend operator fun invoke(): Flow<UsbAction> {
        return merge(
            usbEventFlow(),
            permissionEventFlow()
        )
    }

    private fun usbEventFlow(): Flow<UsbAction> = callbackFlow {

        val usbReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                        intent.parcelable<UsbDevice>(UsbManager.EXTRA_DEVICE)?.let { usbDevice ->
                            trySend(UsbAction.DeviceAttached(usbDevice))

                            // Delay to allow Android to auto-grant "Always allow" permission
                            coroutineScope.launch {
                                Timber.d("hasPermission BEFORE delay = ${usbManager.hasPermission(usbDevice)}") // false
                                delay(150)
                                Timber.d("hasPermission AFTER delay = ${usbManager.hasPermission(usbDevice)}") // true

                                if (usbManager.hasPermission(usbDevice)) {
                                    trySend(UsbAction.PermissionGranted(usbDevice))
                                } else if (HardwareConfiguration.isSupported(usbDevice)) {
                                    requestUsbPermissionUseCase(usbDevice)
                                }
                            }
                        }
                    }
                    UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                        intent.parcelable<UsbDevice>(UsbManager.EXTRA_DEVICE)?.let { usbDevice ->
                            trySend(UsbAction.DeviceDetached(usbDevice))
                        } ?: trySend(UsbAction.DeviceDetached())
                    }
                }
            }
        }

        context.registerReceiver(
            usbReceiver,
            IntentFilter().apply {
                addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
                addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
            },
            Context.RECEIVER_NOT_EXPORTED
        )

        // Initial check: emit attached devices only if supported
        usbManager.deviceList.values
            .filter { HardwareConfiguration.isSupported(it) }
            .forEach { usbDevice ->
                trySend(UsbAction.DeviceAttached(usbDevice))
                if (usbManager.hasPermission(usbDevice)) {
                    trySend(UsbAction.PermissionGranted(usbDevice))
                }
            }

        awaitClose { context.unregisterReceiver(usbReceiver) }
    }

    private fun permissionEventFlow(): Flow<UsbAction> = usbPermissionEvents

    companion object {
        private val _usbPermissionEvents = MutableSharedFlow<UsbAction>(replay = 0)
        val usbPermissionEvents: SharedFlow<UsbAction> = _usbPermissionEvents

        suspend fun emit(action: UsbAction) {
            _usbPermissionEvents.emit(action)
        }
    }
}
