package com.turtlpass.module.chooser.ui

import android.content.res.Configuration
import android.view.Window
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Right
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.MainActivity
import com.turtlpass.R
import com.turtlpass.common.compose.rememberBottomSheetNavigator
import com.turtlpass.common.compose.systemui.SystemUi
import com.turtlpass.common.domain.Result
import com.turtlpass.module.chooser.AccountsPermission
import com.turtlpass.module.chooser.ChooserUiState
import com.turtlpass.module.chooser.PermissionState
import com.turtlpass.module.chooser.UsbPermission
import com.turtlpass.module.chooser.UsbState
import com.turtlpass.module.chooser.model.ChooserInputs
import com.turtlpass.module.chooser.navigation.ChooserDestination
import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.module.installedapp.ui.InstalledAppsScreen
import com.turtlpass.module.loader.LoaderType
import com.turtlpass.module.loader.ui.LoaderScreen
import com.turtlpass.module.passphrase.PassphraseScreen
import com.turtlpass.module.passphrase.PassphraseUiState
import com.turtlpass.module.passphrase.PassphraseViewModel
import com.turtlpass.module.passphrase.model.PassphraseInput
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

@FlowPreview
@ExperimentalTextApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPermissionsApi
@ExperimentalLifecycleComposeApi
@ExperimentalMaterialNavigationApi
@Composable
fun ChooserScreen(
    window: Window,
    uiState: State<ChooserUiState>,
    passphraseUiState: State<PassphraseUiState>,
    usbState: State<UsbState>,
    permissionState: State<PermissionState>,
    passphraseViewModel: PassphraseViewModel = hiltViewModel(),
    onInstalledApp: (app: InstalledApp) -> Unit,
    onRecentApp: (app: InstalledApp) -> Unit,
    onUserAccount: (account: UserAccount?) -> Unit,
    onStoredAccount: (account: UserAccount) -> Unit,
    storePassphrase: (passphrase: String) -> Unit,
    decryptPassphrase: () -> Unit,
    onPassphraseDecrypted: (passphrase: String) -> Unit,
    onPinCompleted: (pin: List<Int>) -> Unit,
    onWriteUsbSerial: () -> Unit,
    finishApp: () -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val bottomSheetNavigator = rememberBottomSheetNavigator(
        sheetState = sheetState,
        skipHalfExpanded = true,
    )
    val navController = rememberAnimatedNavController()
    navController.navigatorProvider += bottomSheetNavigator

    val popBackStack: () -> Unit = {
        navController.popBackStack(route = ChooserDestination.ScreenMain.name, inclusive = false)
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
    LaunchedEffect(Unit) {
        passphraseViewModel
            .passphraseResult
            .collect { passphrase ->
                onPassphraseDecrypted(passphrase)
                navController.navigate(ChooserDestination.SheetPin.name)
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
                AnimatedNavHost(
                    navController = navController,
                    startDestination = ChooserDestination.ScreenMain.name,
                ) {

                    composable(route = ChooserDestination.ScreenMain.name) {
                        ChooserContentScreen(
                            uiState = uiState,
                            usbState = usbState,
                            permissionState = permissionState,
                            passphraseUiState = passphraseUiState,
                            onRecentApp = onRecentApp,
                            onStoredAccount = onStoredAccount,
                            onAccountSelected = { account ->
                                onUserAccount(account)
                            },
                            onPassphraseDecrypt = decryptPassphrase,
                            navController = navController,
                        )
                    }

                    composable(
                        route = ChooserDestination.ScreenPassphrase.name,
                        enterTransition = { slideIntoContainer(Left, tween(300)) },
                        exitTransition = { slideOutOfContainer(Left, tween(600)) },
                        popEnterTransition = { slideIntoContainer(Right, tween(300)) },
                        popExitTransition = { slideOutOfContainer(Right, tween(600)) }
                    ) {
                        PassphraseScreen(
                            viewModel = passphraseViewModel,
                            onNavigateUpClick = { navController.navigateUp() },
                            onSaveClick = storePassphrase,
                            onPassphraseEncrypted = { isSuccessful ->
                                navController.navigateUp()
                                if (isSuccessful) {
                                    Toast.makeText(
                                        /* context = */ context,
                                        /* text = */ context.getString(R.string.passphrase_saved),
                                        /* duration = */ Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
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
                            if (usbState.value.isUsbConnected
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
                                        popBackStack()
                                        finishApp()
                                    }
                                    LoaderType.Error -> onWriteUsbSerial() // retry
                                    else -> {}
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
@ExperimentalLifecycleComposeApi
@ExperimentalMaterialNavigationApi
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
    backgroundColor = 0xff303030,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
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
        val passphraseUiState = remember {
            mutableStateOf(PassphraseUiState(model = PassphraseInput()))
        }

        ChooserScreen(
            window = MainActivity().window,
            uiState = uiState,
            usbState = usbState,
            passphraseUiState = passphraseUiState,
            permissionState = permissionsState,
            onInstalledApp = {},
            onRecentApp = {},
            onUserAccount = {},
            onStoredAccount = {},
            onPinCompleted = {},
            onWriteUsbSerial = {},
            finishApp = {},
            storePassphrase = {},
            decryptPassphrase = {},
            onPassphraseDecrypted = {},
        )
    }
}
