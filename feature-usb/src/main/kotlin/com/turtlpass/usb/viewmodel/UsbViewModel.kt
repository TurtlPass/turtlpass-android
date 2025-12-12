package com.turtlpass.usb.viewmodel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.usb.model.KeyDerivationInput
import com.turtlpass.usb.model.UsbUiState
import com.turtlpass.usb.model.UsbWriteResult
import com.turtlpass.usb.state.UsbStateProvider
import com.turtlpass.usb.usecase.KeyDerivationUseCase
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
    private val usbStateProvider: UsbStateProvider,
    private val keyDerivationUseCase: KeyDerivationUseCase,
    private val writeUsbSerialUseCase: WriteUsbSerialUseCase,
) : ViewModel() {

    // ONE-SHOT EVENTS
    private val _events = MutableSharedFlow<UsbUiEvent>()
    val events = _events.asSharedFlow()

    val usbUiState: StateFlow<UsbUiState> = usbStateProvider.usbUiState

    // Submit a USB action
    fun submitAction(action: UsbUiAction) {
        when (action) {
            is UsbUiAction.WriteSerialInputs -> writeUsbSerial(action.keyDerivationInput)
//            is UsbAction2.WriteSerialSeed -> //todo
        }
    }

    private fun writeUsbSerial(keyDerivationInput: KeyDerivationInput) {
        viewModelScope.launch {
            _events.emit(UsbUiEvent.UsbWriteLoading)

            val usbDevice = usbStateProvider.getAttachedUsbDevice()
            if (usbDevice == null) {
                _events.emit(UsbUiEvent.UsbWriteError)
                return@launch
            }

            keyDerivationUseCase(keyDerivationInput)
                .flatMapMerge { hash -> writeUsbSerialUseCase(usbDevice, hash) }
                .collect { result ->
                    when (result) {
                        UsbWriteResult.Success -> {
                            _events.emit(UsbUiEvent.UsbWriteSuccess)
                        }
                        UsbWriteResult.Error -> {
                            _events.emit(UsbUiEvent.UsbWriteError)
                        }
                    }
                }
        }
    }
}
