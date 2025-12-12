package com.turtlpass.biometric.biometricPrompt

import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricPromptHandler(
    private val activity: FragmentActivity
) {
    /**
     * Builds a BiometricPrompt with the provided callbacks.
     */
    fun createPrompt(
        onSuccess: () -> Unit = {},
        onFailure: (error: String?) -> Unit = {},
        onCancelled: () -> Unit = {}
    ): BiometricPrompt {

        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailure("Authentication failed")
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                when (errorCode) {
                    BiometricPrompt.ERROR_NEGATIVE_BUTTON,
                    BiometricPrompt.ERROR_USER_CANCELED,
                    BiometricPrompt.ERROR_CANCELED -> onCancelled()
                    else -> onFailure(errString.toString())
                }
            }
        }

        return BiometricPrompt(activity, executor, callback)
    }

    /**
     * Build BiometricPrompt.PromptInfo with correct Android version handling
     */
    fun buildBiometricPromptInfo(
        title: String,
        subtitle: String? = null,
        description: String,
        negativeButtonText: String,
    ): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .apply {
                setTitle(title)
                setSubtitle(subtitle)
                setDescription(description)
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ->
                        setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                    else -> {
                        setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK and BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                    }
                }
                setNegativeButtonText(negativeButtonText)
            }.build()
    }

    /**
     * Shows the biometric prompt
     */
    fun authenticate(
        biometricPrompt: BiometricPrompt,
        promptInfo: BiometricPrompt.PromptInfo,
        cryptoObject: BiometricPrompt.CryptoObject? = null
    ) {
        cryptoObject?.let {
            biometricPrompt.authenticate(promptInfo, cryptoObject)
        } ?: biometricPrompt.authenticate(promptInfo)
    }
}