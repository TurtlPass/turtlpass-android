package com.turtlpass.module.chooser.viewmodel

import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.usb.model.UsbUiState
import com.turtlpass.usb.state.UsbStateProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@OptIn(
    ExperimentalCoroutinesApi::class,
    ExperimentalPermissionsApi::class,
)
@HiltViewModel
class ChooserViewModel @Inject constructor(
    usbStateProvider: UsbStateProvider,
) : ViewModel() {
    val usbUiState: StateFlow<UsbUiState> = usbStateProvider.usbUiState
}
