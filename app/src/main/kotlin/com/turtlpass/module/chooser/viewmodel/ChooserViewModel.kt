package com.turtlpass.module.chooser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.turtlpass.module.accessibility.usecase.AccessibilityUpdatesUseCase
import com.turtlpass.module.accessibility.usecase.PackageUpdatesUseCase
import com.turtlpass.module.chooser.AccountsPermission
import com.turtlpass.module.chooser.ChooserUiState
import com.turtlpass.module.chooser.PermissionState
import com.turtlpass.module.chooser.UsbPermission
import com.turtlpass.module.chooser.UsbState
import com.turtlpass.module.chooser.UsbWriteResult
import com.turtlpass.module.chooser.model.ChooserInputs
import com.turtlpass.module.chooser.usecase.HashUserInputUseCase
import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.module.installedapp.usecase.InstalledAppsUseCase
import com.turtlpass.module.usb.UsbAction
import com.turtlpass.module.usb.usecase.UsbUpdatesUseCase
import com.turtlpass.module.usb.usecase.WriteUsbSerialUseCase
import com.turtlpass.module.useraccount.model.UserAccount
import com.turtlpass.module.useraccount.usecase.RetrieveAccountUseCase
import com.turtlpass.module.useraccount.usecase.UserAccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPermissionsApi
@HiltViewModel
class ChooserViewModel @Inject constructor(
    private val installedAppsUseCase: InstalledAppsUseCase,
    private val userAccountsUseCase: UserAccountsUseCase,
    private val retrieveAccountUseCase: RetrieveAccountUseCase,
    private val packageUpdatesUseCase: PackageUpdatesUseCase,
    private val accessibilityUpdatesUseCase: AccessibilityUpdatesUseCase,
    private val usbUpdatesUseCase: UsbUpdatesUseCase,
    private val hashUserInputUseCase: HashUserInputUseCase,
    private val writeUsbSerialUseCase: WriteUsbSerialUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ChooserUiState> by lazy {
        MutableStateFlow(ChooserUiState(model = ChooserInputs()))
    }
    val uiState: StateFlow<ChooserUiState> by lazy { _uiState.asStateFlow() }

    private val _permissionState: MutableStateFlow<PermissionState> by lazy {
        MutableStateFlow(PermissionState())
    }
    val permissionState: StateFlow<PermissionState> by lazy { _permissionState.asStateFlow() }

    private val _usbState: MutableStateFlow<UsbState> by lazy { MutableStateFlow(UsbState()) }
    val usbState: StateFlow<UsbState> by lazy { _usbState.asStateFlow() }


    init {
        viewModelScope.launch {
            installedAppsUseCase()
                .onEach { result ->
                    _uiState.update { state -> state.copy(installedAppsResult = result) }
                }
                .launchIn(this)

            userAccountsUseCase()
                .onEach { result ->
                    _uiState.update { state -> state.copy(userAccountsResult = result) }
                }
                .launchIn(this)

            accessibilityUpdatesUseCase()
                .buffer(Channel.CONFLATED) // handle back pressure by dropping oldest
                .filterNotNull()
                .distinctUntilChanged() // filter out subsequent repetition of values
                .onEach { result ->
                    _uiState.update { state ->
                        state.copy(isAccessibilityServiceEnabled = result)
                    }
                }
                .launchIn(this)

            packageUpdatesUseCase()
                .buffer(Channel.CONFLATED)
                .filterNotNull()
                .distinctUntilChanged()
                .onEach { app ->
                    if (app != uiState.value.model.installedApp) {
                        _uiState.update { state ->
                            state.copy(model = state.model.copy(recentApp = app))
                        }
                    }
                }
                .launchIn(this)

            usbUpdatesUseCase()
                .buffer(Channel.CONFLATED)
                .filterNotNull()
                .distinctUntilChanged()
                .onEach { usbAction ->
                    processUsbUpdate(usbAction)
                }
                .launchIn(this)
        }
    }

    private fun processUsbUpdate(usbAction: UsbAction) {
        when (usbAction) {
            is UsbAction.DeviceAttached -> {
                _usbState.update { state ->
                    state.copy(
                        isUsbConnected = true,
                        usbDevice = usbAction.usbDevice
                    )
                }
            }
            UsbAction.DeviceDetached -> {
                _usbState.update { state ->
                    state.copy(
                        isUsbConnected = false,
                        usbDevice = null
                    )
                }
            }
            UsbAction.PermissionGranted -> {
                _usbState.update { state -> state.copy(usbPermission = UsbPermission.Granted) }
            }
            UsbAction.PermissionNotGranted -> {
                _usbState.update { state -> state.copy(usbPermission = UsbPermission.NotGranted) }
            }
        }
    }

    fun selectRecentApp(app: InstalledApp) {
        _uiState.update { state ->
            state.copy(model = state.model.copy(installedApp = app, recentApp = null))
        }
        getSelectedAccount(app)
    }

    fun selectStoredAccount(account: UserAccount) {
        _uiState.update { state ->
            state.copy(model = state.model.copy(userAccount = account, storedAccount = null))
        }
    }

    fun updateInstalledApp(app: InstalledApp) {
        _uiState.update { state -> state.copy(model = state.model.copy(installedApp = app)) }
        getSelectedAccount(app)
    }

    private fun getSelectedAccount(app: InstalledApp) {
        viewModelScope.launch {
            retrieveAccountUseCase(app.packageName)
                .collect { result ->
                    _uiState.update { state ->
                        state.copy(model = state.model.copy(storedAccount = result))
                    }
                }
        }
    }

    fun updateUserAccount(account: UserAccount?) {
        _uiState.update { state -> state.copy(model = state.model.copy(userAccount = account)) }
    }

    private fun getUserAccounts() {
        viewModelScope.launch {
            userAccountsUseCase()
                .onEach { result ->
                    _uiState.update { state -> state.copy(userAccountsResult = result) }
                }
                .launchIn(this)
        }
    }

    fun updatePin(pin: List<Int>) {
        _uiState.update { state ->
            state.copy(model = state.model.copy(pin = pin.joinToString(separator = "")))
        }
    }

    fun updatePassphrase(passphrase: String) {
        _uiState.update { state ->
            state.copy(model = state.model.copy(passphrase = passphrase))
        }
    }

    @FlowPreview
    fun writeUsbSerial() {
        _usbState.update { state -> state.copy(usbWriteResult = null) }

        viewModelScope.launch {
            _usbState.value.usbDevice?.let { usbDevice ->
                hashUserInputUseCase(chooserInputs = uiState.value.model)
                    .flatMapMerge { hash ->
                        writeUsbSerialUseCase(usbDevice, hash)
                    }
                    .collect { result ->
                        _usbState.update { state ->
                            state.copy(usbWriteResult = result)
                        }
                        if (result == UsbWriteResult.Success) {
                            _uiState.update { state -> state.copy(model = ChooserInputs()) }
                        }
                    }
            }
        }
    }

    fun onPermissionsResult(permissions: List<String>, permissionsResult: Map<String, Boolean>) {
        val allPermissionsGranted = permissions.all { permissionsResult[it] == true }
        if (allPermissionsGranted) getUserAccounts()
        _permissionState.update { state ->
            state.copy(
                accountsPermission = when {
                    allPermissionsGranted -> AccountsPermission.Granted
                    else -> AccountsPermission.NotGranted
                }
            )
        }
    }

    fun onPermissionEvent(multiplePermissionsState: MultiplePermissionsState) {
        with(multiplePermissionsState) {
            if (allPermissionsGranted && shouldShowRationale.not()) {
                launchMultiplePermissionRequest()
                _permissionState.update { state ->
                    state.copy(accountsPermission = AccountsPermission.Granted)
                }
            } else {
                _permissionState.update { state ->
                    state.copy(
                        accountsPermission = AccountsPermission.Rationale,
                        multiplePermissionsState = this
                    )
                }
            }
        }
    }
}
