package com.turtlpass.biometric.usecase

import android.util.Base64
import androidx.biometric.BiometricPrompt
import com.turtlpass.biometric.repository.PinRepository
import com.turtlpass.di.IoDispatcher
import com.turtlpass.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RetrieveDecryptedPinUseCase @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val pinRepository: PinRepository,
) {
    operator fun invoke(
        cryptoObject: BiometricPrompt.CryptoObject
    ): Flow<Result<String>> {
        return flow {
            try {
                val cipher = cryptoObject.cipher
                requireNotNull(cipher) { "Cipher must not be null in cryptoObject" }

                // retrieve and decode stored Base64 values
                val encryptedBase64 = pinRepository.retrievePin()
                    ?: return@flow emit(Result.Error(Exception("Missing encrypted pin")))
                val encBytes = Base64.decode(encryptedBase64, Base64.NO_WRAP)

                // decrypt
                val decryptedBytes = cipher.doFinal(encBytes)
                val decryptedText = decryptedBytes.toString(Charsets.UTF_8)

                // emit result
                emit(Result.Success(decryptedText))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.catch {
            emit(Result.Error(it))
        }.flowOn(dispatcher)
    }
}