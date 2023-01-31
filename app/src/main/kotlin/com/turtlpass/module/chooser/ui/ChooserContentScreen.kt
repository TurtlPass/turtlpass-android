package com.turtlpass.module.chooser.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.R
import com.turtlpass.common.compose.button.PrimaryButton
import com.turtlpass.common.domain.Result
import com.turtlpass.module.chooser.AccountsPermission
import com.turtlpass.module.chooser.ChooserUiState
import com.turtlpass.module.chooser.PermissionState
import com.turtlpass.module.chooser.UsbPermission
import com.turtlpass.module.chooser.UsbState
import com.turtlpass.module.chooser.model.ChooserInputs
import com.turtlpass.module.chooser.navigation.ChooserDestination
import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.module.passphrase.PassphraseUiState
import com.turtlpass.module.passphrase.model.PassphraseInput
import com.turtlpass.module.useraccount.model.UserAccount
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.icons.Fingerprint
import com.turtlpass.theme.icons.Security

@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ChooserContentScreen(
    uiState: State<ChooserUiState>,
    usbState: State<UsbState>,
    permissionState: State<com.turtlpass.module.chooser.PermissionState>,
    passphraseUiState: State<PassphraseUiState>,
    onRecentApp: (app: InstalledApp) -> Unit,
    onAccountSelected: (account: UserAccount?) -> Unit,
    onStoredAccount: (account: UserAccount) -> Unit,
    onPassphraseDecrypt: () -> Unit,
    navController: NavHostController,
) {
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .fillMaxSize()
            .background(colors.default.background)
            .padding(top = dimensions.x32)
    ) {
        PulseHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.x16)
                .padding(bottom = dimensions.x8),
            usbState = usbState
        )
        InstalledAppField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.x32)
                .padding(top = dimensions.x2),
            uiState = uiState,
            onClick = {
                navController.navigate(ChooserDestination.SheetApps.name)
            },
        )
        AnimatedVisibility(visible = uiState.value.model.recentApp != null) {
            uiState.value.model.recentApp?.let { recentApp ->
                RecentApp(
                    modifier = Modifier
                        .padding(
                            start = dimensions.x32 + dimensions.x16,
                            top = dimensions.x8,
                        ),
                    recentApp = recentApp,
                    onRecentApp = onRecentApp,
                )
            }
        }
        UserAccountField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.x32)
                .padding(top = dimensions.x16),
            uiState = uiState,
            isSheetOpen = navController.currentBackStackEntry?.destination?.route
                    == ChooserDestination.SheetAccounts.name,
            onAccountSelected = onAccountSelected,
            onTrailingIconClick = {
                if (permissionState.value.accountsPermission == AccountsPermission.Granted) {
                    navController.navigate(ChooserDestination.SheetAccounts.name)
                }
            },
        )
        AnimatedVisibility(visible = uiState.value.model.storedAccount != null) {
            uiState.value.model.storedAccount?.let { storedAccount ->
                StoredAccount(
                    modifier = Modifier
                        .padding(
                            top = dimensions.x8,
                            start = dimensions.x32 + dimensions.x16,
                        ),
                    userAccount = storedAccount,
                    onStoredAccount = onStoredAccount,
                )
            }
        }
        PrimaryButton(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .align(alignment = Alignment.End)
                .padding(horizontal = dimensions.x32)
                .padding(
                    top = dimensions.x16 + dimensions.x8,
                    bottom = dimensions.x16
                ),
            text = stringResource(R.string.button_unlock),
            imageVector = if (passphraseUiState.value.isBiometric()) {
                Icons.Rounded.Fingerprint
            } else Icons.Rounded.Security,
            enabled = uiState.value.model.installedApp != null
                    && uiState.value.model.userAccount != null,
            onClick = {
                focusManager.clearFocus()
                if (passphraseUiState.value.isPassphraseNeeded()) {
                    onPassphraseDecrypt()
                } else {
                    navController.navigate(ChooserDestination.SheetPin.name)
                }
            },
            backgroundColor = colors.default.button
        )
        NotificationsContainer(
            permissionState = permissionState,
            uiState = uiState,
            passphraseUiState = passphraseUiState,
            onPassphraseClick = {
                navController.navigate(ChooserDestination.ScreenPassphrase.name)
            }
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPermissionsApi
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
                        recentApp = InstalledApp("Telegram", ""),
                        installedApp = InstalledApp("TurtlPass", "com.turtlpass"),
                        userAccount = UserAccount("hello@turtlpass.com", null),
                        storedAccount = UserAccount("android@turtlpass.com", null),
                    ),
                    isAccessibilityServiceEnabled = true,
                    installedAppsResult = Result.Success(emptyList())
                )
            )
        }
        val usbState = remember {
            mutableStateOf(
                UsbState(usbPermission = UsbPermission.NotGranted)
            )
        }
        val permissionsState = remember {
            mutableStateOf(
                PermissionState(accountsPermission = AccountsPermission.Rationale)
            )
        }
        val passphraseUiState = remember {
            mutableStateOf(PassphraseUiState(model = PassphraseInput()))
        }
        val navController = rememberNavController()

        ChooserContentScreen(
            uiState = uiState,
            usbState = usbState,
            permissionState = permissionsState,
            passphraseUiState = passphraseUiState,
            onAccountSelected = {},
            onStoredAccount = {},
            onRecentApp = {},
            onPassphraseDecrypt = {},
            navController = navController,
        )
    }
}
