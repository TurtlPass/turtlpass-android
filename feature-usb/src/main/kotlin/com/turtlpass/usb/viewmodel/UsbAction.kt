package com.turtlpass.usb.viewmodel

import com.turtlpass.usb.model.KeyDerivationInput

sealed interface UsbAction {
    object RequestPermission : UsbAction
    data class WriteSerialInputs(val keyDerivationInput: KeyDerivationInput) : UsbAction
//    data class WriteSerialSeed() : UsbAction
}
