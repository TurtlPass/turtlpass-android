package com.turtlpass.module.passphrase.usecase

import android.util.Base64
import androidx.biometric.BiometricPrompt
import com.turtlpass.common.domain.Result
import com.turtlpass.di.IoDispatcher
import com.turtlpass.module.passphrase.repo.PassphraseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RetrieveDecryptedPassphraseUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val passphraseRepository: PassphraseRepository,
) {
    operator fun invoke(
        cryptoObject: BiometricPrompt.CryptoObject
    ): Flow<Result<String>> {
        return flow {
            try {
                // read encrypted passphrase (string base64 encoded)
                val encPassphrase = passphraseRepository.retrievePassphrase()
                // decode passphrase on byteArray
                val encTokenData = Base64.decode(encPassphrase, Base64.DEFAULT)
                // decrypt passphrase using cipher inside cryptoObject
                val decrypted = decrypt(encTokenData, cryptoObject)
                if (decrypted == null) {
                    emit(Result.Error(Exception("Failed to decrypt passphrase")))
                    return@flow
                }
                // emit result
                emit(Result.Success(decrypted))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.catch {
            emit(Result.Error(it))
        }.flowOn(dispatcher)
    }

    private fun decrypt(
        encryptedData: ByteArray,
        cryptoObject: BiometricPrompt.CryptoObject
    ): String? {
        cryptoObject.cipher?.apply {
            val decrypted = doFinal(encryptedData)
            return decrypted.toString(Charsets.UTF_8)
        }
        return null
    }
}
