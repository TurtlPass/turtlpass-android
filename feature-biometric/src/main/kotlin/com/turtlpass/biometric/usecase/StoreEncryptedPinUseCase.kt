package com.turtlpass.biometric.usecase

import android.util.Base64
import androidx.biometric.BiometricPrompt
import com.turtlpass.biometric.repository.PinRepository
import com.turtlpass.di.IoDispatcher
import com.turtlpass.domain.Result
import com.turtlpass.domain.sha512
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class StoreEncryptedPinUseCase @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val pinRepository: PinRepository,
) {
    operator fun invoke(
        pin: String,
        cryptoObject: BiometricPrompt.CryptoObject
    ): Flow<Result<Unit>> {
        return flow {
            try {
                // hash pin
                val pinHash = sha512(pin)

                // encrypt the hash
                val cipher = cryptoObject.cipher
                requireNotNull(cipher) { "Cipher must not be null in cryptoObject" }
                val encryptedBytes = cipher.doFinal(pinHash.toByteArray(Charsets.UTF_8))
                val ivBytes = cipher.iv

                // store Base64 encoded ciphertext and iv
                val encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
                val ivBase64 = Base64.encodeToString(ivBytes, Base64.NO_WRAP)

                pinRepository.persistPin(encryptedBase64)
                pinRepository.persistInitializationVector(ivBase64)

                // emit result
                emit(Result.Success(Unit))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.catch {
            emit(Result.Error(it))
        }.flowOn(dispatcher)
    }
}