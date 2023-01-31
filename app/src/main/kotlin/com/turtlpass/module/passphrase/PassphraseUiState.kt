package com.turtlpass.module.passphrase

import com.turtlpass.module.passphrase.model.PassphraseInput

data class PassphraseUiState(
    val model: PassphraseInput,
    val isBiometricAvailable: Boolean = false,
    val isPassphraseEnabled: Boolean = false,
    val passphraseEncryptResult: Boolean? = null,
    val passphraseDecryptResult: String? = null,
) {
    fun isBiometric(): Boolean {
        return isBiometricAvailable && isPassphraseEnabled
    }

    fun isPassphraseNeeded(): Boolean {
        return isBiometricAvailable && isPassphraseEnabled && passphraseDecryptResult.isNullOrEmpty()
    }
}
