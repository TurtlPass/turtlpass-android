package com.turtlpass.usb.model

import turtlpass.Turtlpass

sealed class UsbWriteResult {
    object Success : UsbWriteResult()
    data class Error(val errorCode: Turtlpass.ErrorCode) : UsbWriteResult()
}
