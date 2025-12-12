package com.turtlpass.usb.state

import android.hardware.usb.UsbDevice
import com.turtlpass.usb.model.UsbPermission
import com.turtlpass.usb.model.UsbUiState
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class UsbStateProviderImpl @Inject constructor(
    // inject the mutable state flow (provided by Hilt module)
    private val _usbUiState: MutableStateFlow<UsbUiState>
) : UsbStateProvider {

    override val usbUiState: StateFlow<UsbUiState> = _usbUiState.asStateFlow()

    override fun isUsbConnected(): Boolean = _usbUiState.value.isUsbConnected

    override fun hasUsbPermission(): Boolean = _usbUiState.value.usbPermission == UsbPermission.Granted

    override fun isUsbReady(): Boolean = isUsbConnected() && hasUsbPermission()

    override fun getAttachedUsbDevice(): UsbDevice? = _usbUiState.value.usbDevice

    // Called by the collector to update the state
    fun updateState(update: UsbUiState.() -> UsbUiState) {
        _usbUiState.update(update)
    }

    /** Internal mutation API – NOT in interface */
    // Only UsbStateTracker / hardware layer gets access to update()
    fun update(block: UsbUiState.() -> UsbUiState) {
        _usbUiState.update(block)
    }
}
