package com.turtlpass.module.biometric.usecase

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.turtlpass.common.domain.Result
import com.turtlpass.di.ApplicationScope
import com.turtlpass.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber
import javax.inject.Inject

class BiometricAuthenticationUseCase @Inject constructor(
    @MainDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val coroutineScope: CoroutineScope,
) {
    suspend operator fun invoke(
        activity: FragmentActivity,
        promptInfo: BiometricPrompt.PromptInfo,
        cryptoObject: BiometricPrompt.CryptoObject?,
    ): Flow<Result<BiometricPrompt.AuthenticationResult>> {
        return callbackFlow {
            val callback = object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    trySend(Result.Error(SecurityException(errString.toString())))
                    cancel()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    trySend(Result.Success(result))
                    cancel()
                }

                override fun onAuthenticationFailed() {
                    trySend(Result.Error())
                    cancel()
                }
            }
            Timber.d("Start biometric authentication")
            val biometricPrompt = BiometricPrompt(
                /* activity = */ activity,
                /* executor = */ ContextCompat.getMainExecutor(activity.baseContext),
                /* callback = */ callback
            )
            cryptoObject?.let {
                biometricPrompt.authenticate(promptInfo, cryptoObject)
            } ?: biometricPrompt.authenticate(promptInfo)

            awaitClose {
                Timber.d("Stop biometric authentication")
            }
        }.flowOn(dispatcher)
            .shareIn( // make cold flow hot
                coroutineScope, // make the flow follow the externalScope
                replay = 0,
                started = SharingStarted.WhileSubscribed() // keep the producer active while there are active subscribers
            )
    }
}
