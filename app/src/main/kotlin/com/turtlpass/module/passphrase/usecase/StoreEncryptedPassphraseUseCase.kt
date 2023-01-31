package com.turtlpass.module.passphrase.usecase

import android.util.Base64
import androidx.biometric.BiometricPrompt
import com.turtlpass.common.domain.Result
import com.turtlpass.common.extension.sha512
import com.turtlpass.di.IoDispatcher
import com.turtlpass.module.passphrase.repo.PassphraseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class StoreEncryptedPassphraseUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val passphraseRepository: PassphraseRepository,
) {
    operator fun invoke(
        passphrase: String,
        cryptoObject: BiometricPrompt.CryptoObject
    ): Flow<Result<Unit>> {
        return flow {
            try {
                // hash passphrase
                val passphraseHash = sha512(passphrase)
                // encrypt passphrase using the cipher inside the cryptoObject
                val encrypted = encrypt(passphraseHash, cryptoObject)
                if (encrypted == null) {
                    emit(Result.Error(Exception("Failed to encrypt passphrase")))
                    return@flow
                }
                // store encrypted passphrase and iv
                val passphraseBase64 = Base64.encodeToString(encrypted.first, Base64.DEFAULT)
                val ivBase64 = Base64.encodeToString(encrypted.second, Base64.DEFAULT)
                passphraseRepository.persistPassphrase(passphraseBase64)
                passphraseRepository.persistInitializationVector(ivBase64)
                // emit result
                emit(Result.Success(Unit))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.catch {
            emit(Result.Error(it))
        }.flowOn(dispatcher)
    }

    private fun encrypt(
        clearText: String,
        cryptoObject: BiometricPrompt.CryptoObject
    ): Pair<ByteArray, ByteArray>? {
        cryptoObject.cipher?.apply {
            val encrypted = doFinal(clearText.toByteArray(Charsets.UTF_8))
            return Pair(
                first = encrypted,
                second = iv
            )
        }
        return null
    }
}
