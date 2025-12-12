package com.turtlpass.biometric.crypto

import android.content.pm.PackageManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricPrompt.CryptoObject
import timber.log.Timber
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class CryptoManager @Inject constructor(
    private val packageManager: PackageManager,
) {
    fun validateKey(): CryptoValidationResult {
        return if (!isKeystoreKeyPresent()) {
            generateSecretKey()
        } else {
            warmUp()
        }
    }

    private fun generateSecretKey(): CryptoValidationResult {
        return try {
            // Clean up old key if any
            removeKeystoreKey()

            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE).apply {
                init(buildKeyGenSpec())
                generateKey()
            }
            CryptoValidationResult.Success
        } catch (e: Exception) {
            Timber.e(e, "Failed to generate keystore key")
            CryptoValidationResult.KeyInitFail
        }
    }

    private fun buildKeyGenSpec(): KeyGenParameterSpec {
        val builder = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(true)
            .setRandomizedEncryptionRequired(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            builder.setUnlockedDeviceRequired(true)
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_STRONGBOX_KEYSTORE)) {
                builder.setIsStrongBoxBacked(true)
            }
        }
        return builder.build()
    }

    private fun warmUp(): CryptoValidationResult {
        return try {
            val iv = ByteArray(12).also { SecureRandom().nextBytes(it) } // GCM needs 12 bytes IV
            createCryptoObject(CryptoOperation.Decrypt, iv)
            CryptoValidationResult.Success

        } catch (e: KeyPermanentlyInvalidatedException) {
            Timber.w(e, "Key permanently invalidated")
            CryptoValidationResult.KeyPermanentlyInvalidated
        } catch (e: Exception) {
            Timber.e(e, "Error warming up keystore key")
            CryptoValidationResult.Error
        }
    }

    fun createCryptoObject(cryptoOperation: CryptoOperation, iv: ByteArray?): CryptoObject {
        val cipher = getCipher()
        val secretKey = getSecretKey()

        when (cryptoOperation) {
            CryptoOperation.Decrypt -> {
                requireNotNull(iv) { "IV must not be null for decryption" }
                cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
            }
            CryptoOperation.Encrypt -> {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            }
        }
        return CryptoObject(cipher)
    }

    private fun getCipher(): Cipher = Cipher.getInstance(
        "${KeyProperties.KEY_ALGORITHM_AES}/" +
                "${KeyProperties.BLOCK_MODE_GCM}/" +
                KeyProperties.ENCRYPTION_PADDING_NONE
    )

    private fun getKeystore(): KeyStore {
        return KeyStore.getInstance(KEYSTORE).apply { load(null) }
    }

    private fun getSecretKey(): SecretKey {
        return getKeystore().getKey(KEY_ALIAS, null) as SecretKey
    }

    private fun isKeystoreKeyPresent(): Boolean {
        return getKeystore().isKeyEntry(KEY_ALIAS)
    }

    private fun removeKeystoreKey() {
        runCatching { getKeystore().deleteEntry(KEY_ALIAS) }
            .onFailure { Timber.w(it, "Failed to delete keystore key") }
    }

    fun clear() = removeKeystoreKey()

    companion object {
        const val KEYSTORE = "AndroidKeyStore"
        const val KEY_ALIAS = "DefEncDecKey"
    }
}
