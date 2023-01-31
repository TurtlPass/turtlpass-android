package com.turtlpass.module.passphrase

import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.R
import com.turtlpass.common.domain.Result
import com.turtlpass.module.biometric.CryptoOperation
import com.turtlpass.module.biometric.usecase.BiometricAuthenticationUseCase
import com.turtlpass.module.biometric.usecase.IsBiometricReadyUseCase
import com.turtlpass.module.biometric.usecase.IsPassphraseEnabledUseCase
import com.turtlpass.module.biometric.usecase.PrepareKeyStoreUseCase
import com.turtlpass.module.passphrase.model.PassphraseInput
import com.turtlpass.module.passphrase.usecase.RetrieveDecryptedPassphraseUseCase
import com.turtlpass.module.passphrase.usecase.StoreEncryptedPassphraseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalPermissionsApi
@HiltViewModel
class PassphraseViewModel @Inject constructor(
    private val isBiometricReadyUseCase: IsBiometricReadyUseCase,
    private val isPassphraseEnabledUseCase: IsPassphraseEnabledUseCase,
    private val prepareKeyStoreUseCase: PrepareKeyStoreUseCase,
    private val biometricAuthenticationUseCase: BiometricAuthenticationUseCase,
    private val storeEncryptedPassphraseUseCase: StoreEncryptedPassphraseUseCase,
    private val retrieveDecryptedPassphraseUseCase: RetrieveDecryptedPassphraseUseCase,
) : ViewModel() {

    private val _uiState by lazy { MutableStateFlow(PassphraseUiState(model = PassphraseInput())) }
    val uiState: StateFlow<PassphraseUiState> by lazy { _uiState.asStateFlow() }

    private val _passphraseResult by lazy { MutableSharedFlow<String>() }
    val passphraseResult by lazy { _passphraseResult.asSharedFlow() }

    init {
        viewModelScope.launch {
            isBiometricReadyUseCase()
                .collect { result ->
                    _uiState.update { state ->
                        state.copy(isBiometricAvailable = result.isSuccessful())
                    }
                }
            isPassphraseEnabledUseCase()
                .collect { result ->
                    _uiState.update { state ->
                        state.copy(isPassphraseEnabled = result.isSuccessful())
                    }
                }
        }
    }

    fun encryptPassphraseWithBiometric(activity: FragmentActivity, passphrase: String) {
        viewModelScope.launch {
            prepareKeyStoreUseCase(CryptoOperation.Encrypt)
                .flatMapMerge { cryptoObjectResult ->
                    when (cryptoObjectResult) {
                        is Result.Success -> {
                            biometricAuthenticationUseCase(
                                activity = activity,
                                promptInfo = buildBiometricPromptInfo(
                                    title = activity.getString(R.string.lock_passphrase),
                                    description = activity.getString(R.string.lock_passphrase_message),
                                    negativeButton = activity.getString(R.string.button_cancel)
                                ),
                                cryptoObject = cryptoObjectResult.data
                            )
                        }
                        else -> emptyFlow()
                    }
                }.flatMapMerge { authenticationResult ->
                    when (authenticationResult) {
                        is Result.Success -> {
                            authenticationResult.data.cryptoObject?.let { cryptoObject ->
                                storeEncryptedPassphraseUseCase(passphrase, cryptoObject)
                            } ?: emptyFlow()
                        }
                        else -> emptyFlow()
                    }
                }.collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    passphraseEncryptResult = true,
                                    isPassphraseEnabled = true
                                )
                            }
                        }
                        is Result.Error -> {
                            _uiState.update { state ->
                                state.copy(
                                    passphraseEncryptResult = false,
                                    isPassphraseEnabled = false
                                )
                            }
                        }
                        else -> {}
                    }
                }
        }
    }

    fun decryptPassphraseWithBiometric(activity: FragmentActivity) {
        viewModelScope.launch {
            prepareKeyStoreUseCase(CryptoOperation.Decrypt)
                .flatMapMerge { cryptoObjectResult ->
                    when (cryptoObjectResult) {
                        is Result.Success -> {
                            biometricAuthenticationUseCase(
                                activity = activity,
                                promptInfo = buildBiometricPromptInfo(
                                    title = activity.getString(R.string.unlock_passphrase),
                                    description = activity.getString(R.string.unlock_passphrase_message),
                                    negativeButton = activity.getString(R.string.button_cancel)
                                ),
                                cryptoObject = cryptoObjectResult.data
                            )
                        }
                        else -> emptyFlow()
                    }
                }.flatMapMerge { authenticationResult ->
                    when (authenticationResult) {
                        is Result.Success -> {
                            authenticationResult.data.cryptoObject?.let { cryptoObject ->
                                retrieveDecryptedPassphraseUseCase(cryptoObject)
                            } ?: emptyFlow()
                        }
                        else -> emptyFlow()
                    }
                }.collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.update { state ->
                                state.copy(passphraseDecryptResult = result.data)
                            }
                            _passphraseResult.emit(result.data)
                        }
                        else -> {}
                    }
                }
        }
    }

    private fun buildBiometricPromptInfo(
        title: String,
        subtitle: String? = null,
        description: String,
        negativeButton: String,
    ): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .apply {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ->
                        setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                    else -> {
                        setNegativeButtonText(negativeButton)
                    }
                }
            }.build()
    }
}
