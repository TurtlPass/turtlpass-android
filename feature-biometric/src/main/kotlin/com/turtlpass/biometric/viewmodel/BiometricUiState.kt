package com.turtlpass.biometric.viewmodel

data class BiometricUiState(
    val isBiometricAvailable: Boolean = false,
    val isBiometricEnabled: Boolean = false,
    val decryptedPin: String? = null,
)
