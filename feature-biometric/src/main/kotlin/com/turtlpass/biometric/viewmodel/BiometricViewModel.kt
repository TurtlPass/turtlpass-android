package com.turtlpass.biometric.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.biometric.R
import com.turtlpass.biometric.biometricPrompt.BiometricPromptHandler
import com.turtlpass.biometric.crypto.CryptoOperation
import com.turtlpass.biometric.usecase.IsBiometricReadyUseCase
import com.turtlpass.biometric.usecase.IsPinPersistedUseCase
import com.turtlpass.biometric.usecase.PrepareKeyStoreUseCase
import com.turtlpass.biometric.usecase.RetrieveDecryptedPinUseCase
import com.turtlpass.biometric.usecase.StoreEncryptedPinUseCase
import com.turtlpass.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalPermissionsApi::class,
)
@HiltViewModel
class BiometricViewModel @Inject constructor(
    private val isBiometricReadyUseCase: IsBiometricReadyUseCase,
    private val isBiometricEnabledUseCase: IsPinPersistedUseCase,
    private val prepareKeyStoreUseCase: PrepareKeyStoreUseCase,
    private val encryptUseCase: StoreEncryptedPinUseCase,
    private val decryptUseCase: RetrieveDecryptedPinUseCase,
) : ViewModel() {

    // ------------------------------------------------------------
    // UI STATE
    // ------------------------------------------------------------
    private val _uiState = MutableStateFlow(BiometricUiState())
    val uiState: StateFlow<BiometricUiState> = _uiState.asStateFlow()

    // ------------------------------------------------------------
    // ONE-SHOT EVENTS
    // ------------------------------------------------------------
    private val _events = MutableSharedFlow<BiometricUiEvent>(
        replay = 0,
        extraBufferCapacity = 1 // buffer one event if no collector is active
    )
    val events = _events.asSharedFlow()

    private lateinit var promptHandler: BiometricPromptHandler


    init {
        viewModelScope.launch {
            launch {
                isBiometricReadyUseCase().collect { result ->
                    _uiState.update { it.copy(isBiometricAvailable = result.isSuccessful()) }
                }
            }
            launch {
                isBiometricEnabledUseCase().collect { result ->
                    _uiState.update { it.copy(isBiometricEnabled = result.isSuccessful()) }
                }
            }
        }
    }

    init {
        observeBiometricReadiness()
        observeBiometricAvailability()
    }

    // ------------------------------------------------------------
    // INITIALIZATION FLOWS
    // ------------------------------------------------------------
    private fun observeBiometricReadiness() {
        viewModelScope.launch {
            isBiometricReadyUseCase().collect { result ->
                _uiState.update { it.copy(isBiometricAvailable = result.isSuccessful()) }
            }
        }
    }

    private fun observeBiometricAvailability() {
        viewModelScope.launch {
            isBiometricEnabledUseCase().collect { result ->
                _uiState.update { it.copy(isBiometricEnabled = result.isSuccessful()) }
            }
        }
    }

    fun initHandler(activity: FragmentActivity) {
        promptHandler = BiometricPromptHandler(activity)
    }

    fun submitAction(action: BiometricAction) {
        when (action) {
            is BiometricAction.Encrypt -> handleEncrypt(action.activity, action.pin)
            is BiometricAction.Decrypt -> handleDecrypt(action.activity)
            is BiometricAction.SetEnabled -> setEnabled(action.enabled)
            BiometricAction.ClearDecrypted -> clearDecrypted()
        }
    }

    private fun handleEncrypt(activity: FragmentActivity, pin: String) {
        viewModelScope.launch {
            prepareKeyStoreUseCase(CryptoOperation.Encrypt).collect { cryptoResult ->
                when (cryptoResult) {
                    is Result.Success -> {
                        val promptInfo = promptHandler.buildBiometricPromptInfo(
                            title = activity.getString(R.string.feature_biometric_lock_pin),
                            description = activity.getString(R.string.feature_biometric_lock_pin_message),
                            negativeButtonText = activity.getString(R.string.feature_biometric_button_cancel)
                        )
                        val prompt = promptHandler.createPrompt(
                            onSuccess = {
                                viewModelScope.launch {
                                    encryptUseCase(pin, cryptoResult.data)
                                        .collect { encryptResult ->
                                            when (encryptResult) {
                                                is Result.Success -> {
                                                    _uiState.update { it.copy(isBiometricEnabled = true) }
                                                    _events.emit(
                                                        BiometricUiEvent.EncryptionSuccess(
                                                            pin
                                                        )
                                                    )
                                                }

                                                is Result.Error -> {
                                                    _events.emit(
                                                        BiometricUiEvent.Error(
                                                            encryptResult.error?.message
                                                        )
                                                    )
                                                }

                                                else -> {}
                                            }
                                        }
                                }
                            },
                            onFailure = {
                                Timber.e("Authentication failed")
                                _events.tryEmit(BiometricUiEvent.Error("Authentication failed"))
                            },
                            onCancelled = {
                                Timber.e(">> EncryptionCancelled")
                                _events.tryEmit(BiometricUiEvent.EncryptionCancelled)
                            }
                        )
                        promptHandler.authenticate(prompt, promptInfo, cryptoResult.data)
                    }

                    is Result.Error -> _events.emit(BiometricUiEvent.Error(cryptoResult.error?.message))
                    else -> {}
                }
            }
        }
    }

    private fun handleDecrypt(activity: FragmentActivity) {
        viewModelScope.launch {
            prepareKeyStoreUseCase(CryptoOperation.Decrypt).collect { cryptoResult ->
                when (cryptoResult) {
                    is Result.Success -> {
                        val promptInfo = promptHandler.buildBiometricPromptInfo(
                            title = activity.getString(R.string.feature_biometric_unlock_pin),
                            description = activity.getString(R.string.feature_biometric_unlock_pin_message),
                            negativeButtonText = activity.getString(R.string.feature_biometric_button_cancel)
                        )
                        val prompt = promptHandler.createPrompt(
                            onSuccess = {
                                viewModelScope.launch {
                                    decryptUseCase(cryptoResult.data).collect { decryptResult ->
                                        when (decryptResult) {
                                            is Result.Success -> {
                                                _uiState.update { it.copy(decryptedPin = decryptResult.data) }
                                                _events.emit(
                                                    BiometricUiEvent.DecryptionSuccess(
                                                        decryptResult.data
                                                    )
                                                )
                                            }

                                            is Result.Error -> {
                                                _events.emit(BiometricUiEvent.Error(decryptResult.error?.message))
                                            }

                                            else -> {}
                                        }
                                    }
                                }
                            },
                            onFailure = {
                                Timber.e("Authentication failed")
                                _events.tryEmit(BiometricUiEvent.Error("Authentication failed"))
                            },
                            onCancelled = {
                                Timber.e(">> DecryptionCancelled")
                                _events.tryEmit(BiometricUiEvent.DecryptionCancelled)
                            }
                        )
                        promptHandler.authenticate(prompt, promptInfo, cryptoResult.data)
                    }

                    is Result.Error -> _events.emit(BiometricUiEvent.Error(cryptoResult.error?.message))
                    else -> {}
                }
            }
        }
    }

    private fun setEnabled(enabled: Boolean) {
        _uiState.update { it.copy(isBiometricEnabled = enabled) }
    }

    private fun clearDecrypted() {
        _uiState.update { it.copy(decryptedPin = null) }
    }
}
