package com.turtlpass.module.biometric.usecase

import android.util.Base64
import androidx.biometric.BiometricPrompt
import com.turtlpass.common.domain.Result
import com.turtlpass.di.IoDispatcher
import com.turtlpass.module.biometric.CryptoOperation
import com.turtlpass.module.biometric.CryptoManager
import com.turtlpass.module.biometric.CryptoValidationResult
import com.turtlpass.module.passphrase.repo.PassphraseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PrepareKeyStoreUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val passphraseRepository: PassphraseRepository,
    private val cryptoManager: CryptoManager,
) {
    operator fun invoke(purpose: CryptoOperation): Flow<Result<BiometricPrompt.CryptoObject>> {
        return flow {
            try {
                // validateKey crypto layer
                when (cryptoManager.validateKey()) {
                    CryptoValidationResult.KeyPermanentlyInvalidated,
                    CryptoValidationResult.KeyInitFail -> {
                        clearCryptoAndData()
                        return@flow
                    }
                    else -> {}
                }
                // create CryptoObject
                val cryptoObject = createCryptoObject(purpose)
                // emit result
                emit(Result.Success(cryptoObject))

            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.catch {
            emit(Result.Error(it))
        }.flowOn(dispatcher)
    }

    private fun createCryptoObject(
        purpose: CryptoOperation
    ): BiometricPrompt.CryptoObject {
        return cryptoManager.createCryptoObject(
            cryptoOperation = purpose,
            iv = when (purpose) {
                CryptoOperation.Decrypt -> {
                    Base64.decode(
                        passphraseRepository.retrieveInitializationVector(),
                        Base64.DEFAULT
                    )
                }
                else -> null
            }
        )
    }

    private fun clearCryptoAndData() {
        cryptoManager.clear()
        passphraseRepository.clear()
    }
}
