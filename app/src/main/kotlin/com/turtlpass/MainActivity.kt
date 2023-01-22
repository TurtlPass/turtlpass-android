package com.turtlpass

import android.Manifest.permission.GET_ACCOUNTS
import android.Manifest.permission.READ_CONTACTS
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.turtlpass.module.chooser.ui.ChooserScreen
import com.turtlpass.module.chooser.viewmodel.ChooserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalTextApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPermissionsApi
@ExperimentalMaterialNavigationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<ChooserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // handle the splash screen transition
        super.onCreate(savedInstanceState)
        setContent {
            val permissions = remember { listOf(GET_ACCOUNTS, READ_CONTACTS) }
            val multiplePermissionsState = rememberMultiplePermissionsState(
                permissions = permissions,
                onPermissionsResult = { permissionsResult ->
                    viewModel.onPermissionsResult(permissions, permissionsResult)
                }
            )
            LaunchedEffect(key1 = multiplePermissionsState) {
                viewModel.onPermissionEvent(multiplePermissionsState)
            }
            ChooserScreen(
                window = window,
                uiState = viewModel.uiState.collectAsState(),
                usbState = viewModel.usbState.collectAsState(),
                permissionState = viewModel.permissionState.collectAsState(),
                onInstalledApp = { app -> viewModel.updateInstalledApp(app) },
                onRecentApp = { app -> viewModel.selectRecentApp(app) },
                onUserAccount = { account -> viewModel.updateUserAccount(account) },
                onStoredAccount = { account -> viewModel.selectStoredAccount(account) },
                onPinCompleted = { pin -> viewModel.updatePin(pin) },
                onWriteUsbSerial = { viewModel.writeUsbSerial() },
                finishApp = { finish() }
            )
        }
    }
}
