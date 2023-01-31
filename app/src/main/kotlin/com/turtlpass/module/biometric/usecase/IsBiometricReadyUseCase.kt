package com.turtlpass.module.biometric.usecase

import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import com.turtlpass.common.domain.Result
import com.turtlpass.di.IoDispatcher
import com.turtlpass.module.biometric.CryptoManager
import com.turtlpass.module.biometric.CryptoValidationResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class IsBiometricReadyUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val biometricManager: BiometricManager,
    private val cryptoManager: CryptoManager,
) {
    operator fun invoke(): Flow<Result<Boolean>> {
        return flow {
            try {
                val authenticationStatus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    biometricManager.canAuthenticate(BIOMETRIC_STRONG)
                } else {
                    biometricManager.canAuthenticate(BIOMETRIC_WEAK)
                }
                if (authenticationStatus != BiometricManager.BIOMETRIC_SUCCESS) {
                    emit(Result.Error(Exception("biometric cannot authenticate")))
                    return@flow
                }
                val keyStatus = cryptoManager.validateKey() == CryptoValidationResult.Success
                if (keyStatus.not()) {
                    emit(Result.Error(Exception("KeyStore invalid")))
                    return@flow
                }
                emit(Result.Success(true))

            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.catch {
            emit(Result.Error(it))
        }.flowOn(dispatcher)
    }
}
