package com.turtlpass.module.selection.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.biometric.ui.UserPinScreen
import com.turtlpass.biometric.viewmodel.BiometricUiState
import com.turtlpass.domain.Result
import com.turtlpass.module.selection.deviceFlow.DeviceFlowMode
import com.turtlpass.module.selection.deviceFlow.DeviceScreenContainer
import com.turtlpass.module.selection.deviceFlow.deviceDestination
import com.turtlpass.module.selection.model.SelectionUiState
import com.turtlpass.module.selection.ui.SelectedInputsScreen
import com.turtlpass.usb.model.UsbUiState
import com.turtlpass.usb.ui.ConnectUsbScreen
import com.turtlpass.usb.ui.LoaderScreen
import com.turtlpass.usb.ui.LoaderType
import com.turtlpass.useraccount.model.UserAccount
import com.turtlpass.useraccount.ui.UserAccountsScreen
import com.turtlpass.useraccount.viewmodel.UserAccountUiState

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalTextApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun SelectionNavigationScreens(
    flowMode: DeviceFlowMode,
    uiState: State<SelectionUiState>,
    selectionUiState: State<SelectionUiState>,
    userAccountUiState: State<UserAccountUiState>,
    biometricUiState: State<BiometricUiState>,
    usbUiState: State<UsbUiState>,
    navController: NavHostController,
    title: @Composable () -> String = { "" },
    onAccountPickerRequested: () -> Unit,
    onUserAccount: (account: UserAccount?) -> Unit,
    popBackStack: () -> Unit,
    onPinCompleted: (pin: List<Int>, enableFingerprint: Boolean) -> Unit,
    onFingerprint: () -> Unit,
    onWriteUsbSerial: () -> Unit,
    finish: () -> Unit,
) {
    NavHost(navController, startDestination = NavigationDeviceItem.Selection.route) {
        deviceDestination(mode = flowMode, route = NavigationDeviceItem.Selection.route) {
            DeviceScreenContainer(
                mode = flowMode,
                usbUiState = usbUiState,
            ) {
                BackHandler { finish() }
                SelectedInputsScreen(
                    selectionUiState = selectionUiState,
                    userAccountUiState = userAccountUiState,
                    onUserAccount = onUserAccount,
                    onCancel = finish,
                    onNavigateUserAccounts = onAccountPickerRequested,
                    onGetPassword = {
                        if (biometricUiState.value.isBiometricAvailable
                            && biometricUiState.value.isBiometricEnabled
                        ) {
                            onFingerprint()
                        } else {
                            navController.navigate(NavigationDeviceItem.PIN.route)
                        }
                    },
                )
            }
        }
        deviceDestination(
            mode = DeviceFlowMode.BottomSheet,
            route = NavigationDeviceItem.UserAccounts.route
        ) {
            userAccountUiState.value.userAccountsResult.let { result ->
                UserAccountsScreen(
                    accountList = if (result is Result.Success) result.data else emptyList(),
                    onAccountSelected = { accountItem ->
                        onUserAccount(accountItem)
                        popBackStack()
                    }
                )
            }
        }
        deviceDestination(mode = flowMode, route = NavigationDeviceItem.PIN.route) {
            DeviceScreenContainer(
                mode = flowMode,
                usbUiState = usbUiState,
                onCancel = finish,
                title = title()
            ) {
                BackHandler { finish() }
                UserPinScreen(
                    modifier = if (flowMode == DeviceFlowMode.FullScreen) Modifier.fillMaxHeight()
                    else Modifier.wrapContentHeight(),
                    biometricUiState = biometricUiState,
                    onPinCompleted = { pin, enableFingerprint ->
                        onPinCompleted(pin, enableFingerprint)
                    },
                    onFingerprint = onFingerprint,
                )
            }
        }
        deviceDestination(mode = flowMode, route = NavigationDeviceItem.ConnectUsb.route) {
            DeviceScreenContainer(
                mode = flowMode,
                usbUiState = usbUiState,
                onCancel = finish,
                title = title()
            ) {
                BackHandler { finish() }
                ConnectUsbScreen(
                    modifier = if (flowMode == DeviceFlowMode.FullScreen) Modifier.fillMaxHeight()
                    else Modifier.wrapContentHeight(),
                    usbUiState = usbUiState,
                    onReadyClick = {
                        navController.navigate(NavigationDeviceItem.Loader.route)
                        onWriteUsbSerial()
                    }
                )
            }
        }
        deviceDestination(mode = flowMode, route = NavigationDeviceItem.Loader.route) {
            DeviceScreenContainer(
                mode = flowMode,
                usbUiState = usbUiState,
                onCancel = finish,
                title = title()
            ) {
                BackHandler { finish() }
                LoaderScreen(
                    modifier = if (flowMode == DeviceFlowMode.FullScreen) Modifier.fillMaxHeight()
                    else Modifier.wrapContentHeight(),
                    loaderType = uiState.value.loaderType,
                    onClick = { state ->
                        when (state) {
                            LoaderType.Success -> finish()
                            LoaderType.Error -> onWriteUsbSerial() // onRetry
                            else -> {}
                        }
                    }
                )
            }
        }
    }
}
