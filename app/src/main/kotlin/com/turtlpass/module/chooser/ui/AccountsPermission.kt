package com.turtlpass.module.chooser.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.R
import com.turtlpass.module.chooser.AccountsPermission
import com.turtlpass.module.chooser.PermissionState
import com.turtlpass.theme.AppTheme

@Composable
@ExperimentalPermissionsApi
fun AccountsPermissionCard(
    permissionState: State<PermissionState>,
    onSettingsClick: () -> Unit,
) {
    val state = permissionState.value

    AnimatedVisibility(
        visible = state.accountsPermission == AccountsPermission.Rationale,
    ) {
        ActionCard(
            text = stringResource(R.string.rationale_permission),
            buttonText = stringResource(R.string.rationale_permission_button),
            onClick = { state.multiplePermissionsState?.launchMultiplePermissionRequest() }
        )
    }
    AnimatedVisibility(
        visible = state.accountsPermission == AccountsPermission.NotGranted,
    ) {
        ActionCard(
            text = stringResource(R.string.rationale_app_settings),
            buttonText = stringResource(R.string.rationale_app_settings_button),
            onClick = onSettingsClick
        )
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
        val permissionsState =
            remember { mutableStateOf(PermissionState(accountsPermission = AccountsPermission.Rationale)) }

        AccountsPermissionCard(
            permissionState = permissionsState,
            onSettingsClick = {},
        )
    }
}
