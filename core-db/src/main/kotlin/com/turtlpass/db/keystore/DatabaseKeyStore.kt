package com.turtlpass.db.keystore

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.core.content.edit
import java.security.KeyStore
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Manages the SQLCipher database passphrase using Android Keystore.
 *
 * Security model:
 * - A random database passphrase is generated per app install
 * - The passphrase is encrypted using an AES-256 key stored in Android Keystore
 * - Only the encrypted passphrase is persisted to disk
 * - The Keystore key itself never leaves secure hardware (when available)
 *
 * Guarantees:
 * - No user input required
 * - Database is unreadable off-device
 * - Keys are removed automatically on app uninstall
 *
 * Failure behavior:
 * - If the Keystore key becomes invalid (OS update, lock change, OEM bug),
 *   decryption will fail and the caller is expected to recreate the database.
 */
object DatabaseKeyStore {

    private const val KEY_ALIAS = "sqlcipher_db_key"
    private const val PREFS = "db_key_prefs"
    private const val PREF_KEY = "encrypted_key"

    /**
     * Returns the SQLCipher passphrase as a byte array.
     *
     * On first run:
     * - Generates a random passphrase
     * - Encrypts it with a Keystore-backed AES key
     * - Persists the encrypted value
     *
     * On subsequent runs:
     * - Decrypts and returns the stored passphrase
     *
     * @throws java.security.GeneralSecurityException if the Keystore key is invalid or corrupted
     */
    fun getPassphrase(context: Context): ByteArray {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val secretKey = getOrCreateKey(keyStore)
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val encrypted = prefs.getString(PREF_KEY, null)
        if (encrypted != null) {
            return decrypt(encrypted, secretKey)
        }
        val passphrase = UUID.randomUUID().toString()
        prefs.edit {
            putString(PREF_KEY, encrypt(passphrase, secretKey))
        }
        return passphrase.toByteArray(Charsets.UTF_8)
    }

    /**
     * Returns an existing AES-256 key from Android Keystore or creates one if missing.
     */
    private fun getOrCreateKey(keyStore: KeyStore): SecretKey {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val generator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
            generator.init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build()
            )
            generator.generateKey()
        }
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    /**
     * Encrypts the database passphrase using AES/GCM.
     * The returned value contains the IV followed by ciphertext.
     */
    private fun encrypt(value: String, key: SecretKey): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encrypted = cipher.doFinal(value.toByteArray())
        return Base64.encodeToString(cipher.iv + encrypted, Base64.NO_WRAP)
    }

    /**
     * Decrypts the stored database passphrase and converts it to SQLCipher format.
     */
    private fun decrypt(encoded: String, key: SecretKey): ByteArray {
        val data = Base64.decode(encoded, Base64.NO_WRAP)
        val iv = data.copyOfRange(0, 12)
        val encrypted = data.copyOfRange(12, data.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        val decrypted = cipher.doFinal(encrypted)

        return String(decrypted).toByteArray(Charsets.UTF_8)
    }
}
