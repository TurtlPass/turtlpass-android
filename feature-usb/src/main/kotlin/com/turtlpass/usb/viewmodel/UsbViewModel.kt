package com.turtlpass.usb.viewmodel

import android.hardware.usb.UsbManager
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.usb.hardware.HardwareConfiguration
import com.turtlpass.usb.model.KeyDerivationInput
import com.turtlpass.usb.model.UsbUiState
import com.turtlpass.usb.model.UsbWriteResult
import com.turtlpass.usb.state.UsbStateProvider
import com.turtlpass.usb.usecase.KeyDerivationUseCase
import com.turtlpass.usb.usecase.RequestUsbPermissionUseCase
import com.turtlpass.usb.usecase.WriteUsbSerialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(
    ExperimentalCoroutinesApi::class,
    ExperimentalPermissionsApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalTextApi::class,
    FlowPreview::class
)
@HiltViewModel
class UsbViewModel @Inject constructor(
    private val usbManager: UsbManager,
    private val requestUsbPermissionUseCase: RequestUsbPermissionUseCase,
    private val usbStateProvider: UsbStateProvider,
    private val keyDerivationUseCase: KeyDerivationUseCase,
    private val writeUsbSerialUseCase: WriteUsbSerialUseCase,
) : ViewModel() {

    // ONE-SHOT EVENTS
    private val _uiEvents = MutableSharedFlow<UsbUiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    val usbUiState: StateFlow<UsbUiState> = usbStateProvider.usbUiState

    // Submit a USB action
    fun submitAction(action: UsbAction) {
        when (action) {
            is UsbAction.RequestPermission -> requestUsbPermission()
            is UsbAction.WriteSerialInputs -> writeUsbSerial(action.keyDerivationInput)
//            is UsbAction2.WriteSerialSeed -> //todo
        }
    }

    private fun requestUsbPermission() {
        usbManager.deviceList.values
            .filter { HardwareConfiguration.isSupported(it) }
            .forEach { usbDevice ->
                requestUsbPermissionUseCase(usbDevice)
            }
    }

    private fun writeUsbSerial(keyDerivationInput: KeyDerivationInput) {
        viewModelScope.launch {
            _uiEvents.emit(UsbUiEvent.UsbWriteLoading)

            val usbDevice = usbStateProvider.getAttachedUsbDevice()
            if (usbDevice == null) {
                _uiEvents.emit(UsbUiEvent.UsbWriteError(
                    errorMessage = "USB device not attached")
                )
                return@launch
            }

            keyDerivationUseCase(keyDerivationInput)
                .flatMapMerge { hash -> writeUsbSerialUseCase(usbDevice, hash) }
                .collect { result ->
                    when (result) {
                        UsbWriteResult.Success -> {
                            _uiEvents.emit(UsbUiEvent.UsbWriteSuccess)
                        }

                        is UsbWriteResult.Error -> {
                            _uiEvents.emit(UsbUiEvent.UsbWriteError(
                                errorMessage = result.errorCode.toString())
                            )
                        }
                    }
                }
        }
    }
}
