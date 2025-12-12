package com.turtlpass.biometric.viewmodel

sealed interface BiometricUiEvent {

    data class EncryptionSuccess(val pin: String) : BiometricUiEvent

    data class DecryptionSuccess(val pin: String) : BiometricUiEvent

    data object EncryptionCancelled : BiometricUiEvent

    data object DecryptionCancelled : BiometricUiEvent

    data class Error(val message: String? = null) : BiometricUiEvent
}
