package com.turtlpass.biometric.viewmodel

import androidx.fragment.app.FragmentActivity

sealed interface BiometricAction {

    /** User wants to encrypt and store the PIN/passphrase via biometrics */
    data class Encrypt(val activity: FragmentActivity, val pin: String) : BiometricAction

    /** User wants to decrypt the stored passphrase */
    data class Decrypt(val activity: FragmentActivity) : BiometricAction

    /** UI toggles biometric enable/disable state */
    data class SetEnabled(val enabled: Boolean) : BiometricAction

    /** Internal or UI-triggered reset (e.g., logout / disable biometrics) */
    data object ClearDecrypted : BiometricAction
}
