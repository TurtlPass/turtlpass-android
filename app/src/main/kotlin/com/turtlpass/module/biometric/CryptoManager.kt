package com.turtlpass.module.biometric

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
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

class CryptoManager @Inject constructor(
    private val packageManager: PackageManager,
) {
    fun validateKey(): CryptoValidationResult = if (isKeystoreKeyPresent().not())
        generateSecretKey() else warmUp()

    private fun generateSecretKey(): CryptoValidationResult {
        return try {
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE).apply {
                init(keyGenParameterSpec())
                generateKey()
            }
            CryptoValidationResult.Success
        } catch (e: Exception) {
            Timber.e(e)
            CryptoValidationResult.KeyInitFail
        }
    }

    private fun keyGenParameterSpec(): KeyGenParameterSpec {
        return KeyGenParameterSpec.Builder(
            /* keystoreAlias = */ KEY_ALIAS,
            /* purposes = */ KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            // require that every key usage require a user authentication
            // why? https://blog.quarkslab.com/attacking-titan-m-with-only-one-byte.html
            .setUserAuthenticationRequired(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setInvalidatedByBiometricEnrollment(true)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    // ensure key access cannot happen if the device is not unlocked
                    setUnlockedDeviceRequired(true)

                    // protect key with StrongBox security chip (Secure Element)
                    if (packageManager.hasSystemFeature(PackageManager.FEATURE_STRONGBOX_KEYSTORE)) {
                        setIsStrongBoxBacked(true)
                    }
                }
            }
            .build()
    }

    private fun warmUp(): CryptoValidationResult {
        return try {
            val random = SecureRandom()
            val bytes = ByteArray(16)
            random.nextBytes(bytes)
            createCryptoObject(CryptoOperation.Decrypt, bytes)
            CryptoValidationResult.Success
        } catch (e: KeyPermanentlyInvalidatedException) {
            Timber.e(e)
            CryptoValidationResult.KeyPermanentlyInvalidated
        } catch (e: Exception) {
            Timber.e(e)
            CryptoValidationResult.Error
        }
    }

    fun createCryptoObject(cryptoOperation: CryptoOperation, iv: ByteArray?): CryptoObject {
        with(getCipher()) {
            val secretKey = getKeystore().getKey(KEY_ALIAS, null) as SecretKey
            if (cryptoOperation == CryptoOperation.Decrypt) {
                init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
            } else {
                init(Cipher.ENCRYPT_MODE, secretKey)
            }
            return CryptoObject(this)
        }
    }

    private fun getCipher(): Cipher {
        return Cipher.getInstance(
            KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7
        )
    }

    private fun getKeystore(): KeyStore {
        return KeyStore.getInstance(KEYSTORE).apply { load(null) }
    }

    private fun isKeystoreKeyPresent(): Boolean {
        return getKeystore().isKeyEntry(KEY_ALIAS)
    }

    private fun removeKeystoreKey() {
        if (isKeystoreKeyPresent()) getKeystore().deleteEntry(KEY_ALIAS)
    }

    fun clear() = removeKeystoreKey()

    companion object {
        const val KEYSTORE = "AndroidKeyStore"
        const val KEY_ALIAS = "DefEncDecKey"
    }
}
