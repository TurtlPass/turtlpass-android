package com.turtlpass.module.chooser.ui

import android.content.res.Configuration
import android.hardware.usb.UsbDevice
import android.view.Window
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.MainActivity
import com.turtlpass.common.compose.rememberBottomSheetNavigator
import com.turtlpass.common.compose.systemui.SystemUi
import com.turtlpass.common.domain.Result
import com.turtlpass.module.chooser.*
import com.turtlpass.module.chooser.model.ChooserInputs
import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.module.installedapp.ui.InstalledAppsScreen
import com.turtlpass.module.loader.LoaderType
import com.turtlpass.module.loader.ui.LoaderScreen
import com.turtlpass.module.pin.UserPinScreen
import com.turtlpass.module.usb.ui.ConnectUsbScreen
import com.turtlpass.module.useraccount.UserAccountsScreen
import com.turtlpass.module.useraccount.model.UserAccount
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalTextApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPermissionsApi
@ExperimentalMaterialNavigationApi
@Composable
fun ChooserScreen(
    window: Window,
    uiState: State<ChooserUiState>,
    usbState: State<UsbState>,
    permissionState: State<PermissionState>,
    onInstalledApp: (app: InstalledApp) -> Unit,
    onRecentApp: (app: InstalledApp) -> Unit,
    onUserAccount: (account: UserAccount?) -> Unit,
    onStoredAccount: (account: UserAccount) -> Unit,
    onRequestUsbPermission: (usbDevice: UsbDevice) -> Unit,
    onPinCompleted: (pin: List<Int>) -> Unit,
    onWriteUsbSerial: () -> Unit,
    finishApp: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val bottomSheetNavigator = rememberBottomSheetNavigator(
        sheetState = sheetState,
        skipHalfExpanded = true,
    )
    navController.navigatorProvider += bottomSheetNavigator

    val popBackStack: () -> Unit = {
        navController.popBackStack(route = ChooserDestination.MainScreen.name, inclusive = false)
    }
    var isSheetOpened by remember { mutableStateOf(false) }

    BackHandler {
        if (isSheetOpened) {
            popBackStack()
        } else {
            finishApp()
        }
    }

    LaunchedEffect(sheetState.currentValue) {
        when (sheetState.currentValue) {
            ModalBottomSheetValue.Hidden -> {
                isSheetOpened = false
                focusManager.clearFocus()
            }
            else -> {
                isSheetOpened = true
                Timber.i("Bottom sheet ${sheetState.currentValue} state")
            }
        }
    }

    AppTheme {
        SystemUi(window = window) {
            ModalBottomSheetLayout(
                modifier = Modifier
                    .statusBarsPadding(),
                bottomSheetNavigator = bottomSheetNavigator,
                sheetShape = RoundedCornerShape(
                    topStart = dimensions.x16,
                    topEnd = dimensions.x16
                ),
                sheetBackgroundColor = colors.default.sheetBackground,
                scrimColor = colors.default.scrim,
            ) {
                NavHost(
                    navController = navController,
                    startDestination = ChooserDestination.MainScreen.name
                ) {

                    composable(route = ChooserDestination.MainScreen.name) {
                        ChooserContentScreen(
                            uiState = uiState,
                            usbState = usbState,
                            permissionState = permissionState,
                            onRecentApp = onRecentApp,
                            onStoredAccount = onStoredAccount,
                            onAccountSelected = { account ->
                                onUserAccount(account)
                            },
                            navController = navController,
                            onRequestUsbPermission = onRequestUsbPermission,
                        )
                    }

                    bottomSheet(route = ChooserDestination.SheetApps.name) {
                        uiState.value.installedAppsResult.let { result ->
                            InstalledAppsScreen(
                                appList = if (result is Result.Success) result.data else emptyList(),
                                onAppSelected = { installedApp ->
                                    focusManager.clearFocus()
                                    onInstalledApp(installedApp)
                                    popBackStack()
                                },
                                expandBottomSheet = {
                                    coroutineScope.launch {
                                        delay(100)
                                        sheetState.animateTo(ModalBottomSheetValue.Expanded)
                                    }
                                }
                            )
                        }
                    }

                    bottomSheet(route = ChooserDestination.SheetAccounts.name) {
                        uiState.value.userAccountsResult.let { result ->
                            UserAccountsScreen(
                                accountList = if (result is Result.Success) result.data else emptyList(),
                                onAccountSelected = { accountItem ->
                                    focusManager.clearFocus()
                                    onUserAccount(accountItem)
                                    popBackStack()
                                }
                            )
                        }
                    }

                    bottomSheet(route = ChooserDestination.SheetPin.name) {
                        UserPinScreen(onPinCompleted = { pin ->
                            onPinCompleted(pin)
                            if (usbState.value.usbDevice != null
                                && usbState.value.usbPermission == UsbPermission.Granted
                            ) {
                                navController.navigate(ChooserDestination.SheetLoader.name)
                                onWriteUsbSerial()
                            } else {
                                navController.navigate(ChooserDestination.SheetConnectUsb.name)
                            }
                        })
                    }

                    bottomSheet(route = ChooserDestination.SheetConnectUsb.name) {
                        ConnectUsbScreen(
                            usbState = usbState,
                            onRequestUsbPermission = { usbDevice ->
                                onRequestUsbPermission(usbDevice)
                            },
                            onReadyClick = {
                                navController.navigate(ChooserDestination.SheetLoader.name)
                                onWriteUsbSerial()
                            }
                        )
                    }

                    bottomSheet(route = ChooserDestination.SheetLoader.name) {
                        LoaderScreen(
                            result = usbState.value.usbWriteResult,
                            onClick = { state ->
                                when (state) {
                                    LoaderType.Success -> {
                                        // close app, job done
                                        popBackStack()
                                        finishApp()
                                    }
                                    LoaderType.Error -> {
                                        // retry
                                        onWriteUsbSerial()
                                    }
                                    else -> {
                                        // do nothing
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@FlowPreview
@ExperimentalTextApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPermissionsApi
@ExperimentalMaterialNavigationApi
@Preview(
    name = "Light theme",
    showBackground = true,
    backgroundColor = 0xffffffff,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_XL,
    showSystemUi = false,
)
@Composable
private fun Preview() {
    AppTheme {
        val uiState = remember {
            mutableStateOf(
                ChooserUiState(
                    model = ChooserInputs(
                        installedApp = InstalledApp("TurtlPass", "com.turtlpass"),
                        userAccount = UserAccount("android@turtlpass.com", null),
                    )
                )
            )
        }
        val usbState = remember { mutableStateOf(UsbState()) }
        val permissionsState = remember {
            mutableStateOf(PermissionState(accountsPermission = AccountsPermission.Rationale))
        }

        ChooserScreen(
            window = MainActivity().window,
            uiState = uiState,
            usbState = usbState,
            permissionState = permissionsState,
            onInstalledApp = {},
            onRecentApp = {},
            onUserAccount = {},
            onStoredAccount = {},
            onRequestUsbPermission = {},
            onPinCompleted = {},
            onWriteUsbSerial = {},
            finishApp = {},
        )
    }
}
