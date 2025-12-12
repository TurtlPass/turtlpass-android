package com.turtlpass.biometric.usecase

import android.util.Base64
import androidx.biometric.BiometricPrompt
import com.turtlpass.biometric.crypto.CryptoManager
import com.turtlpass.biometric.crypto.CryptoOperation
import com.turtlpass.biometric.crypto.CryptoValidationResult
import com.turtlpass.biometric.repository.PinRepository
import com.turtlpass.di.IoDispatcher
import com.turtlpass.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class PrepareKeyStoreUseCase @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val pinRepository: PinRepository,
    private val cryptoManager: CryptoManager,
) {
    operator fun invoke(purpose: CryptoOperation): Flow<Result<BiometricPrompt.CryptoObject>> {
        return flow {
            try {
                // ensure keystore is valid
                when (val validation = cryptoManager.validateKey()) {
                    CryptoValidationResult.KeyPermanentlyInvalidated,
                    CryptoValidationResult.KeyInitFail -> {
                        Timber.w("Key invalidated or initialization failed: $validation")
                        clearCryptoAndData()
                        emit(Result.Error(Exception("Key invalidated: data cleared")))
                        return@flow
                    }
                    else -> { /* OK */ }
                }

                // create a crypto object depending on purpose
                val cryptoObject = runCatching { createCryptoObject(purpose) }
                    .getOrElse { throw Exception("Failed to create CryptoObject", it) }

                // emit result
                emit(Result.Success(cryptoObject))

            } catch (e: Exception) {
                Timber.e(e, "Failed to prepare keystore")
                emit(Result.Error(e))
            }
        }.catch { e ->
            Timber.e(e, "Error in PrepareKeyStoreUseCase flow")
            emit(Result.Error(e))
        }.flowOn(dispatcher)
    }

    private suspend fun createCryptoObject(
        purpose: CryptoOperation
    ): BiometricPrompt.CryptoObject {
        val iv: ByteArray? = when (purpose) {
            CryptoOperation.Decrypt -> {
                try {
                    pinRepository.retrieveInitializationVector()?.let {
                        Base64.decode(it, Base64.NO_WRAP)
                    } ?: run {
                        Timber.w("No IV found for decryption: falling back to key clear")
                        clearCryptoAndData()
                        null
                    }
                } catch (e: IllegalArgumentException) {
                    Timber.e(e, "Invalid Base64 IV stored: clearing data")
                    clearCryptoAndData()
                    null
                }
            }
            else -> null
        }
        return cryptoManager.createCryptoObject(
            cryptoOperation = purpose,
            iv = iv
        )
    }

    private suspend fun clearCryptoAndData() {
        Timber.i("Clearing keystore and encrypted data")
        cryptoManager.clear()
        pinRepository.clear()
    }
}
