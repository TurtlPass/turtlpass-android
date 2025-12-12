package com.turtlpass.usb.viewmodel

sealed class UsbUiEvent {
    object UsbWriteLoading : UsbUiEvent()
    object UsbWriteSuccess : UsbUiEvent()
    object UsbWriteError : UsbUiEvent()
}
