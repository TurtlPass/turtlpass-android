package com.turtlpass.module.passphrase.repo

import android.content.SharedPreferences
import javax.inject.Inject

class PassphraseRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun isPassphrasePresent(): Boolean {
        return sharedPreferences.getString(ENCRYPTED_PASSPHRASE, null) != null &&
                sharedPreferences.getString(INITIALIZATION_VECTOR, null) != null
    }

    fun persistPassphrase(passphrase: String) {
        sharedPreferences.edit().putString(ENCRYPTED_PASSPHRASE, passphrase).apply()
    }

    fun persistInitializationVector(initializationVector: String) {
        sharedPreferences.edit().putString(INITIALIZATION_VECTOR, initializationVector).apply()
    }

    fun retrievePassphrase(): String? {
        return sharedPreferences.getString(ENCRYPTED_PASSPHRASE, null)
    }

    fun retrieveInitializationVector(): String? {
        return sharedPreferences.getString(INITIALIZATION_VECTOR, null)
    }

    fun clear() {
        sharedPreferences.edit().apply {
            remove(ENCRYPTED_PASSPHRASE)
            remove(INITIALIZATION_VECTOR)
        }.apply()
    }

    companion object {
        const val ENCRYPTED_PASSPHRASE = "encrypted_passphrase"
        const val INITIALIZATION_VECTOR = "initialization_vector"
    }
}
