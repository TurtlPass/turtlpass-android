package com.turtlpass.module.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.appmanager.viewmodel.AppManagerViewModel
import com.turtlpass.module.chooser.viewmodel.ChooserViewModel
import com.turtlpass.module.main.navigation.NavigationItem
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.urlmanager.viewmodel.UrlManagerViewModel
import com.turtlpass.useraccount.permission.rememberAccountsPermissionRequester
import com.turtlpass.useraccount.viewmodel.UserAccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalTextApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalPermissionsApi::class,
)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val chooserViewModel by viewModels<ChooserViewModel>()
    private val userAccountViewModel by viewModels<UserAccountViewModel>()
    private val appManagerViewModel by viewModels<AppManagerViewModel>()
    private val urlManagerViewModel by viewModels<UrlManagerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // handle the splash screen transition
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppTheme {
                val navController = rememberNavController()

                val requestAccountPermissions = rememberAccountsPermissionRequester(
                    onPermissionsGranted = {
                        userAccountViewModel.updatePermissions(allGranted = true)
                        navController.navigate(NavigationItem.UserAccounts.route)
                    }
                )

                MainScreen(
                    navController = navController,
                    appManagerUiState = appManagerViewModel.uiState.collectAsStateWithLifecycle(),
                    urlManagerUiState = urlManagerViewModel.uiState.collectAsStateWithLifecycle(),
                    usbUiState = chooserViewModel.usbUiState.collectAsStateWithLifecycle(),
                    userAccountUiState = userAccountViewModel.uiState.collectAsStateWithLifecycle(),
                    onAccountPickerRequested = { requestAccountPermissions() },
                    onUserAccount = { account ->
                        userAccountViewModel.selectUserAccount(
                            account = account,
                            persist = true
                        )
                    },
                    onDeleteWebsite = { website -> urlManagerViewModel.deleteWebsite(website) },
                    onClearAllWebsites = { urlManagerViewModel.clearAllWebsites() },
                    finishApp = { finish() }
                )
            }
        }
    }
}