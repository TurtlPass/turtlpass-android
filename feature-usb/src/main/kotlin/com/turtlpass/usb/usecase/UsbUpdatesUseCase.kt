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
import com.turtlpass.usb.model.UsbEvent
import com.turtlpass.usb.receiver.registerReceiverCompat
import com.turtlpass.usb.receiver.unregisterReceiverSafe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

class UsbUpdatesUseCase @Inject constructor(
    @param:ApplicationScope private val coroutineScope: CoroutineScope,
    private val context: Context,
    private val usbManager: UsbManager,
    private val requestUsbPermissionUseCase: RequestUsbPermissionUseCase,
) {
    operator fun invoke(): Flow<UsbEvent> {
        return merge(
            usbEventFlow(),
            permissionEventFlow()
        )
    }

    private fun usbEventFlow(): Flow<UsbEvent> = callbackFlow {

        val usbReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                        intent.parcelable<UsbDevice>(UsbManager.EXTRA_DEVICE)?.let { usbDevice ->
                            coroutineScope.launch {
                                // Delay to allow Android to auto-grant "Always allow" permission
                                delay(150)

                                if (usbManager.hasPermission(usbDevice)) {
                                    trySend(UsbEvent.AttachedWithPermission(usbDevice))

                                } else if (HardwareConfiguration.isSupported(usbDevice)) {
                                    trySend(UsbEvent.AttachedWithoutPermission(usbDevice))
                                    requestUsbPermissionUseCase(usbDevice)
                                }
                            }
                        }
                    }
                    UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                        intent.parcelable<UsbDevice>(UsbManager.EXTRA_DEVICE)?.let { usbDevice ->
                            trySend(UsbEvent.DeviceDetached(usbDevice))
                        } ?: trySend(UsbEvent.DeviceDetached())
                    }
                }
            }
        }

        context.registerReceiverCompat(
            receiver = usbReceiver,
            filter = IntentFilter().apply {
                addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
                addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
            },
            exported = false
        )

        // Initial check: emit attached devices only if supported
        usbManager.deviceList.values
            .filter { HardwareConfiguration.isSupported(it) }
            .forEach { usbDevice ->
                if (usbManager.hasPermission(usbDevice)) {
                    trySend(UsbEvent.AttachedWithPermission(usbDevice))
                } else {
                    trySend(UsbEvent.AttachedWithoutPermission(usbDevice))
                }
            }

        awaitClose { context.unregisterReceiverSafe(usbReceiver) }
    }

    private fun permissionEventFlow(): Flow<UsbEvent> = usbPermissionEvents

    companion object {
        private val _usbPermissionEvents = MutableSharedFlow<UsbEvent>(replay = 0)
        val usbPermissionEvents: SharedFlow<UsbEvent> = _usbPermissionEvents

        suspend fun emit(action: UsbEvent) {
            _usbPermissionEvents.emit(action)
        }
    }
}
