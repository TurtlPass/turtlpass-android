package com.turtlpass.module.selection.ui

import android.content.res.Configuration
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.R
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.biometric.viewmodel.BiometricUiState
import com.turtlpass.module.selection.deviceFlow.DeviceFlowContainer
import com.turtlpass.module.selection.model.SelectionInput
import com.turtlpass.module.selection.model.SelectionUiState
import com.turtlpass.module.selection.navigation.NavigationDeviceItem
import com.turtlpass.module.selection.navigation.SelectionNavigationScreens
import com.turtlpass.ui.bottomSheet.rememberBottomSheetNavigator
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.usb.model.UsbDeviceUiState
import com.turtlpass.usb.model.UsbUiState
import com.turtlpass.useraccount.model.UserAccount
import com.turtlpass.useraccount.viewmodel.AccountsPermission
import com.turtlpass.useraccount.viewmodel.UserAccountUiState

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun SelectionScreen(
    navController: NavHostController,
    selectionUiState: State<SelectionUiState>,
    userAccountUiState: State<UserAccountUiState>,
    biometricUiState: State<BiometricUiState>,
    usbUiState: State<UsbUiState>,
    onAccountPickerRequested: () -> Unit,
    onUserAccount: (account: UserAccount?) -> Unit,
    onPinCompleted: (pin: List<Int>, enableFingerprint: Boolean) -> Unit,
    onFingerprint: () -> Unit,
    onWriteUsbSerial: () -> Unit,
    finish: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded,
        skipHalfExpanded = true,
        confirmValueChange = { false } // blocks drag
    )
    val bottomSheetNavigator = rememberBottomSheetNavigator(
        sheetState = sheetState,
        skipHalfExpanded = true,
    )
    navController.navigatorProvider += bottomSheetNavigator
    val popBackStack: () -> Unit = {
        navController.popBackStack(route = NavigationDeviceItem.Selection.route, inclusive = false)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    DeviceFlowContainer(
        flowMode = selectionUiState.value.flowMode,
        usbUiState = usbUiState,
        bottomSheetNavigator = bottomSheetNavigator,
        finish = finish,
        title = { Title(currentRoute) }
    ) {
        SelectionNavigationScreens(
            flowMode = selectionUiState.value.flowMode,
            uiState = selectionUiState,
            selectionUiState = selectionUiState,
            userAccountUiState = userAccountUiState,
            biometricUiState = biometricUiState,
            title = { Title(currentRoute) },
            onAccountPickerRequested = onAccountPickerRequested,
            onUserAccount = onUserAccount,
            usbUiState = usbUiState,
            navController = navController,
            popBackStack = popBackStack,
            onPinCompleted = onPinCompleted,
            onFingerprint = onFingerprint,
            onWriteUsbSerial = onWriteUsbSerial,
            finish = finish,
        )
    }
}

@Composable
private fun Title(currentRoute: String?): String = when (currentRoute) {
    NavigationDeviceItem.Selection.route,
    NavigationDeviceItem.UserAccounts.route -> "Confirm selection"

    NavigationDeviceItem.PIN.route -> stringResource(R.string.title_enter_pin)
    NavigationDeviceItem.ConnectUsb.route -> stringResource(R.string.feature_usb_connect_usb_device)
    NavigationDeviceItem.Loader.route -> ""
    else -> ""
}

@Preview(
    name = "Light theme",
    showBackground = true,
    backgroundColor = 0xffffffff,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Preview(
    name = "Dark theme",
    showBackground = true,
    backgroundColor = 0xff424242,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Composable
private fun Preview() {
    AppTheme {
        SelectionScreen(
            navController = rememberNavController(),
            selectionUiState = remember {
                mutableStateOf(
                    SelectionUiState(
                        model = SelectionInput(
                            selectedApp = InstalledAppUi(
                                appName = "TurtlPass",
                                packageName = "com.turtlpass",
                            )
                        )
                    )
                )
            },
            userAccountUiState = remember { mutableStateOf(UserAccountUiState(
                selectedAccount = UserAccount("hello@turtlpass.com"),
                accountsPermission = AccountsPermission.Rationale
            )) },
            biometricUiState = remember {
                mutableStateOf(
                    BiometricUiState(
                        isBiometricAvailable = true,
                        isBiometricEnabled = true
                    )
                )
            },
            usbUiState = remember {
                mutableStateOf(UsbUiState(usbDeviceUiState = UsbDeviceUiState.Attached))
            },
            onAccountPickerRequested = {},
            onUserAccount = {},
            onPinCompleted = { _, _ -> },
            onFingerprint = {},
            onWriteUsbSerial = {},
            finish = {},
        )
    }
}
