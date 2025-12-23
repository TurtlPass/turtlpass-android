package com.turtlpass.usb.viewmodel

sealed class UsbUiEvent {
    object UsbWriteLoading : UsbUiEvent()
    object UsbWriteSuccess : UsbUiEvent()
    data class UsbWriteError(val errorMessage: String) : UsbUiEvent()
}
