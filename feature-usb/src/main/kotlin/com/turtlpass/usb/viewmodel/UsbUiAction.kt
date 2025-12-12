package com.turtlpass.usb.viewmodel

import com.turtlpass.usb.model.KeyDerivationInput

sealed interface UsbUiAction {
    data class WriteSerialInputs(val keyDerivationInput: KeyDerivationInput) : UsbUiAction
//    data class WriteSerialSeed() : UsbUiAction
}
