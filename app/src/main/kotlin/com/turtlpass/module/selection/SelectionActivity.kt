package com.turtlpass.module.selection

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.appmanager.viewmodel.AppManagerViewModel
import com.turtlpass.biometric.R
import com.turtlpass.biometric.viewmodel.BiometricAction
import com.turtlpass.biometric.viewmodel.BiometricUiEvent
import com.turtlpass.biometric.viewmodel.BiometricViewModel
import com.turtlpass.module.selection.model.ProductTypeUi
import com.turtlpass.module.selection.navigation.NavigationDeviceItem
import com.turtlpass.module.selection.ui.SelectionScreen
import com.turtlpass.module.selection.viewmodel.SelectionViewModel
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.usb.model.KeyDerivationInput
import com.turtlpass.usb.model.UsbPermission
import com.turtlpass.usb.viewmodel.UsbUiAction
import com.turtlpass.usb.viewmodel.UsbViewModel
import com.turtlpass.useraccount.permission.rememberAccountsPermissionRequester
import com.turtlpass.useraccount.viewmodel.UserAccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalTextApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalPermissionsApi::class,
)
@AndroidEntryPoint
class SelectionActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IS_USB_INTENT = "usb_intent"
        const val EXTRA_SELECTED_APP = "selected_app"
        const val EXTRA_WEB_DOMAIN = "selected_web_domain"
        const val EXTRA_SHARE_URL = "extra_share_url"
    }

    private val selectionViewModel by viewModels<SelectionViewModel>()
    private val userAccountViewModel by viewModels<UserAccountViewModel>()
    private val biometricViewModel by viewModels<BiometricViewModel>()
    private val usbViewModel by viewModels<UsbViewModel>()


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectionViewModel.handleIntent(intent)
        biometricViewModel.initHandler(this)

        setContent {
            AppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val selectionUiState by selectionViewModel.uiState.collectAsStateWithLifecycle()
                val usbState = selectionViewModel.usbUiState.collectAsStateWithLifecycle()

                val requestAccountPermissions = rememberAccountsPermissionRequester(
                    onPermissionsGranted = {
                        userAccountViewModel.updatePermissions(allGranted = true)
                        navController.navigate(NavigationDeviceItem.UserAccounts.route)
                    }
                )

                fun buildKeyDerivationInput(pin: String): KeyDerivationInput {
                    val topLevelDomain = when (selectionUiState.model.productType) {
                        ProductTypeUi.Application -> selectionUiState.model.selectedApp?.topLevelDomain
                            ?: ""

                        ProductTypeUi.Website -> selectionUiState.model.selectedUrl?.topLevelDomain ?: ""
                    }
                    val accountId =
                        userAccountViewModel.uiState.value.selectedAccount?.accountId ?: ""
                    return KeyDerivationInput(topLevelDomain, accountId, pin)
                }

                fun navigateToWriteSerialInputs() {
                    if (usbState.value.isUsbConnected
                        && usbState.value.usbPermission == UsbPermission.Granted
                    ) {
                        navController.navigate(NavigationDeviceItem.Loader.route)

                        selectionViewModel.uiState.value.model.selectedPin?.let { pin ->
                            val input = buildKeyDerivationInput(pin)
                            usbViewModel.submitAction(UsbUiAction.WriteSerialInputs(input))
                        }
                    } else {
                        navController.navigate(NavigationDeviceItem.ConnectUsb.route)
                    }
                }

                // Collect USB events
                LaunchedEffect(Unit) {
                    usbViewModel.events.collect { event ->
                        selectionViewModel.updateUsbUiEvent(event)
                    }
                }

                // Collect Biometric events
                LaunchedEffect(Unit) {
                    biometricViewModel.events.collect { event ->
                        when (event) {
                            is BiometricUiEvent.EncryptionSuccess -> {
                                Toast.makeText(
                                    this@SelectionActivity,
                                    getString(R.string.feature_biometric_pin_saved),
                                    Toast.LENGTH_SHORT
                                ).show()

                                selectionViewModel.updatePin(event.pin)
                                navigateToWriteSerialInputs()
                            }

                            BiometricUiEvent.EncryptionCancelled -> {
                                /* do nothing */
                            }

                            is BiometricUiEvent.DecryptionSuccess -> {
                                selectionViewModel.updatePin(event.pin)
                                navigateToWriteSerialInputs()
                            }

                            BiometricUiEvent.DecryptionCancelled -> {
                                navController.navigateIfNotCurrent(NavigationDeviceItem.PIN.route)
                            }

                            is BiometricUiEvent.Error -> {
                                Timber.e(event.message)
                                Toast.makeText(
                                    this@SelectionActivity,
                                    "An error occurred",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

                SelectionScreen(
                    navController = navController,
                    navBackStackEntry = navBackStackEntry,
                    selectionUiState = selectionViewModel.uiState.collectAsStateWithLifecycle(),
                    userAccountUiState = userAccountViewModel.uiState.collectAsStateWithLifecycle(),
                    biometricUiState = biometricViewModel.uiState.collectAsStateWithLifecycle(),
                    usbUiState = usbState,
                    onAccountPickerRequested = { requestAccountPermissions() },
                    onUserAccount = { account ->
                        userAccountViewModel.selectUserAccount(account, persist = false)
                    },
                    onPinCompleted = { pinList, enableFingerprint ->
                        val pin = pinList.joinToString("")
                        selectionViewModel.updatePin(pin)

                        if (enableFingerprint) {
                            biometricViewModel.submitAction(
                                BiometricAction.Encrypt(activity = this, pin = pin)
                            )
                        } else {
                            navigateToWriteSerialInputs()
                        }
                    },
                    onFingerprint = {
                        biometricViewModel.submitAction(
                            BiometricAction.Decrypt(activity = this)
                        )
                    },
                    onWriteUsbSerial = { // onRetry
                        navigateToWriteSerialInputs()
                    },
                    finish = { finish() },
                )
            }
        }
    }
}

fun NavController.navigateIfNotCurrent(route: String) {
    if (currentBackStackEntry?.destination?.route != route) {
        navigate(route)
    }
}
