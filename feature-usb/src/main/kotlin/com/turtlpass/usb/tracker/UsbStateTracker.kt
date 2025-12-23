package com.turtlpass.usb.tracker

import com.turtlpass.di.ApplicationScope
import com.turtlpass.usb.model.UsbDeviceUiState
import com.turtlpass.usb.model.UsbEvent
import com.turtlpass.usb.model.UsbPermission
import com.turtlpass.usb.state.UsbStateProviderImpl
import com.turtlpass.usb.usecase.UsbUpdatesUseCase
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Singleton
class UsbStateTracker @Inject constructor(
    @ApplicationScope scope: CoroutineScope,
    private val usbStateProvider: UsbStateProviderImpl,
    usbUpdatesUseCase: UsbUpdatesUseCase,
) {
    init {
        scope.launch {
            usbUpdatesUseCase()
                .collect { usbAction ->
                    // populate UsbStateProviderImpl so decision logic has accurate snapshot
                    usbStateProvider.updateState {
                        when (usbAction) {
                            is UsbEvent.AttachedWithPermission -> copy(
                                usbDeviceUiState = UsbDeviceUiState.Attached,
                                isUsbConnected = true,
                                usbPermission = UsbPermission.Granted,
                                usbDevice = usbAction.usbDevice,
                            )

                            is UsbEvent.AttachedWithoutPermission -> copy(
                                usbDeviceUiState = UsbDeviceUiState.AttachedWithoutPermission,
                                isUsbConnected = true,
                                usbPermission = UsbPermission.NotGranted,
                                usbDevice = usbAction.usbDevice
                            )

                            is UsbEvent.DeviceDetached -> copy(
                                usbDeviceUiState = UsbDeviceUiState.Detached,
                                isUsbConnected = false,
                                usbDevice = null,
                                // keep previous usbPermission until permission event arrives
                            )
                        }
                    }
                }
        }
    }
}
