package com.turtlpass.module.chooser.ui

import android.content.Intent
import android.content.res.Configuration
import android.hardware.usb.UsbDevice
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.R
import com.turtlpass.common.domain.Result
import com.turtlpass.module.chooser.*
import com.turtlpass.module.chooser.model.ChooserInputs
import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.module.useraccount.model.UserAccount
import com.turtlpass.theme.AppTheme

@ExperimentalPermissionsApi
@Composable
fun NotificationsContainer(
    modifier: Modifier = Modifier,
    uiState: State<ChooserUiState>,
    usbState: State<UsbState>,
    permissionState: State<PermissionState>,
    onRequestUsbPermission: (usbDevice: UsbDevice) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        // USB Permission
        AnimatedVisibility(
            visible = usbState.value.usbDevice != null
                    && usbState.value.usbPermission == UsbPermission.NotGranted
        ) {
            ActionCard(
                text = stringResource(R.string.rationale_usb),
                buttonText = stringResource(R.string.rationale_usb_button),
                onClick = {
                    usbState.value.usbDevice?.let { usbDevice ->
                        onRequestUsbPermission(usbDevice)
                    }
                },
            )
        }
        // Accounts Permission
        AccountsPermissionCard(
            permissionState = permissionState,
            onSettingsClick = {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:" + context.packageName)
                    context.startActivity(this)
                }
            },
        )
        // Accessibility Service
        AnimatedVisibility(visible = uiState.value.isAccessibilityServiceEnabled.not()) {
            ActionCard(
                text = stringResource(R.string.rationale_accessibility),
                buttonText = stringResource(R.string.rationale_accessibility_button),
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                },
            )
        }
    }
}

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
        val usbState =
            remember { mutableStateOf(UsbState(usbPermission = UsbPermission.NotGranted)) }
        val permissionsState =
            remember { mutableStateOf(PermissionState(accountsPermission = AccountsPermission.Rationale)) }

        NotificationsContainer(
            uiState = uiState,
            usbState = usbState,
            permissionState = permissionsState,
            onRequestUsbPermission = {},
        )
    }
}
