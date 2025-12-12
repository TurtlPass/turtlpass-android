package com.turtlpass.usb.state

import android.hardware.usb.UsbDevice
import com.turtlpass.usb.model.UsbUiState
import kotlinx.coroutines.flow.StateFlow

interface UsbStateProvider {

    /** Full observable USB state */
    val usbUiState: StateFlow<UsbUiState>

    fun isUsbConnected(): Boolean
    fun hasUsbPermission(): Boolean
    fun isUsbReady(): Boolean
    fun getAttachedUsbDevice(): UsbDevice?
}
