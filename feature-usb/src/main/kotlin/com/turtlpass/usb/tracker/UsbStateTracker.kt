package com.turtlpass.usb.tracker

import com.turtlpass.di.ApplicationScope
import com.turtlpass.usb.model.UsbAction
import com.turtlpass.usb.model.UsbDeviceUiState
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
                            is UsbAction.DeviceAttached -> copy(
                                usbDeviceUiState = UsbDeviceUiState.Attached,
                                isUsbConnected = true,
                                usbDevice = usbAction.usbDevice,
                                // keep previous permission until permission event arrives
                            )

                            is UsbAction.DeviceDetached -> copy(
                                usbDeviceUiState = UsbDeviceUiState.Detached,
                                isUsbConnected = false,
                                usbDevice = null,
                                //usbPermission = UsbPermission.Unknown
                            )

                            is UsbAction.PermissionGranted -> copy(
                                usbDeviceUiState = UsbDeviceUiState.Attached,
                                usbPermission = UsbPermission.Granted,
                                usbDevice = usbAction.usbDevice
                            )

                            is UsbAction.PermissionNotGranted -> copy(
                                usbDeviceUiState = UsbDeviceUiState.MissingPermission,
                                usbPermission = UsbPermission.NotGranted,
                                usbDevice = usbAction.usbDevice
                            )
                        }
                    }
                }
        }
    }
}
