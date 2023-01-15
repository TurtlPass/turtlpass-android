package com.turtlpass.module.usb.usecase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.turtlpass.BuildConfig
import com.turtlpass.common.extension.parcelable
import com.turtlpass.di.ApplicationScope
import com.turtlpass.module.usb.UsbAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber
import javax.inject.Inject

class UsbUpdatesUseCase @Inject constructor(
    private val context: Context,
    @ApplicationScope private val coroutineScope: CoroutineScope,
) {
    suspend operator fun invoke(): Flow<UsbAction> {
        return callbackFlow {
            val usbReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    when (intent.action) {
                        ACTION_USB_PERMISSION -> {
                            val extra = intent.extras ?: return
                            if (extra.getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED)) {
                                trySend(UsbAction.PermissionGranted)
                            } else {
                                trySend(UsbAction.PermissionNotGranted)
                            }
                        }
                        UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                            intent.parcelable<UsbDevice>(UsbManager.EXTRA_DEVICE)
                                ?.let { usbDevice ->
                                    trySend(UsbAction.DeviceAttached(usbDevice))
                                }
                        }
                        UsbManager.ACTION_USB_DEVICE_DETACHED -> trySend(UsbAction.DeviceDetached)
                        else -> Timber.w("Unknown USB Action")
                    }
                }
            }
            context.registerReceiver(usbReceiver, IntentFilter().apply {
                addAction(ACTION_USB_PERMISSION)
                addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
                addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
            })
            awaitClose {
                context.unregisterReceiver(usbReceiver)
            }
        }.shareIn( // make cold flow hot
            coroutineScope, // make the flow follow the externalScope
            replay = 0,
            started = SharingStarted.WhileSubscribed() // keep the producer active while there are active subscribers
        )
    }

    companion object {
        const val ACTION_USB_PERMISSION = "${BuildConfig.APPLICATION_ID}.USB_PERMISSION"
    }
}
