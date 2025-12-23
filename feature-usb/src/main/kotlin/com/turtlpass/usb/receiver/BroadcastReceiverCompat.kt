package com.turtlpass.usb.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Build

fun Context.registerReceiverCompat(
    receiver: BroadcastReceiver,
    filter: IntentFilter,
    exported: Boolean = false
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val flags = if (exported) {
            Context.RECEIVER_EXPORTED
        } else {
            Context.RECEIVER_NOT_EXPORTED
        }
        registerReceiver(receiver, filter, flags)
    } else {
        @SuppressLint("UnspecifiedRegisterReceiverFlag")
        registerReceiver(receiver, filter)
    }
}

fun Context.unregisterReceiverSafe(receiver: BroadcastReceiver) {
    try {
        unregisterReceiver(receiver)
    } catch (_: IllegalArgumentException) {
        // Receiver was not registered or already unregistered
    }
}
