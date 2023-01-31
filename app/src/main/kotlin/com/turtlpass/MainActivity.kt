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
import androidx.compose.runtime.remember
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.turtlpass.module.chooser.ui.ChooserScreen
import com.turtlpass.module.chooser.viewmodel.ChooserViewModel
import com.turtlpass.module.passphrase.PassphraseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalTextApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPermissionsApi
@ExperimentalLifecycleComposeApi
@ExperimentalMaterialNavigationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val chooserViewModel by viewModels<ChooserViewModel>()
    private val passphraseViewModel by viewModels<PassphraseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // handle the splash screen transition
        super.onCreate(savedInstanceState)

        setContent {
            val permissions = remember { listOf(GET_ACCOUNTS, READ_CONTACTS) }
            val multiplePermissionsState = rememberMultiplePermissionsState(
                permissions = permissions,
                onPermissionsResult = { permissionsResult ->
                    chooserViewModel.onPermissionsResult(permissions, permissionsResult)
                }
            )
            LaunchedEffect(key1 = multiplePermissionsState) {
                chooserViewModel.onPermissionEvent(multiplePermissionsState)
            }
            ChooserScreen(
                window = window,
                uiState = chooserViewModel.uiState.collectAsStateWithLifecycle(),
                usbState = chooserViewModel.usbState.collectAsStateWithLifecycle(),
                passphraseUiState = passphraseViewModel.uiState.collectAsStateWithLifecycle(),
                permissionState = chooserViewModel.permissionState.collectAsStateWithLifecycle(),
                passphraseViewModel = passphraseViewModel,
                onInstalledApp = { app -> chooserViewModel.updateInstalledApp(app) },
                onRecentApp = { app -> chooserViewModel.selectRecentApp(app) },
                onUserAccount = { account -> chooserViewModel.updateUserAccount(account) },
                onStoredAccount = { account -> chooserViewModel.selectStoredAccount(account) },
                storePassphrase = { passphrase ->
                    passphraseViewModel.encryptPassphraseWithBiometric(this, passphrase)
                },
                decryptPassphrase = {
                    passphraseViewModel.decryptPassphraseWithBiometric(this)
                },
                onPassphraseDecrypted = { passphrase ->
                    chooserViewModel.updatePassphrase(passphrase)
                },
                onPinCompleted = { pin -> chooserViewModel.updatePin(pin) },
                onWriteUsbSerial = { chooserViewModel.writeUsbSerial() },
                finishApp = { finish() },
            )
        }
    }
}
