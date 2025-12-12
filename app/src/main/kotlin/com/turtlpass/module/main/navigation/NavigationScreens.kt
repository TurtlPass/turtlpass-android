package com.turtlpass.module.main.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.appmanager.ui.ForegroundAppScreen
import com.turtlpass.appmanager.viewmodel.AppManagerUiState
import com.turtlpass.domain.Result
import com.turtlpass.model.WebsiteUi
import com.turtlpass.urlmanager.ui.UrlListScreen
import com.turtlpass.urlmanager.viewmodel.UrlManagerUiState
import com.turtlpass.useraccount.model.UserAccount
import com.turtlpass.useraccount.ui.UserAccountsScreen
import com.turtlpass.useraccount.viewmodel.UserAccountUiState
import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalTextApi::class, ExperimentalAnimationApi::class,
    ExperimentalPermissionsApi::class, ExperimentalCoroutinesApi::class, FlowPreview::class
)
@Composable
fun NavigationScreens(
    hazeState: HazeState,
    topAppBarState: TopAppBarState,
    userAccountUiState: State<UserAccountUiState>,
    appManagerUiState: State<AppManagerUiState>,
    urlManagerUiState: State<UrlManagerUiState>,
    navController: NavHostController,
    onUserSelected: (account: UserAccount) -> Unit,
    onAppSelected: (app: InstalledAppUi) -> Unit,
    onSelectWebsite: (websiteUi: WebsiteUi) -> Unit,
    onDeleteWebsite: (websiteUi: WebsiteUi) -> Unit,
    onClearAllWebsites: () -> Unit,
    popBackStack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    NavHost(navController, startDestination = NavigationItem.App.route) {

        composable(route = NavigationItem.App.route) {
            ForegroundAppScreen(
                appManagerUiState = appManagerUiState,
                hazeState = hazeState,
                topAppBarState = topAppBarState,
                onAppSelected = { installedApp ->
                    onAppSelected(installedApp)
                },
            )
        }

        bottomSheet(route = NavigationItem.UserAccounts.route) {
            userAccountUiState.value.userAccountsResult.let { result ->
                UserAccountsScreen(
                    accountList = if (result is Result.Success) result.data else emptyList(),
                    onAccountSelected = { accountItem ->
                        onUserSelected(accountItem)
                        popBackStack()
                    }
                )
            }
        }

        composable(route = NavigationItem.Domain.route) {
            UrlListScreen(
                urlManagerUiState = urlManagerUiState,
                hazeState = hazeState,
                onSelectWebsite = onSelectWebsite,
                onDeleteWebsite = onDeleteWebsite,
                onClearAll = onClearAllWebsites,
            )
        }
    }
}
