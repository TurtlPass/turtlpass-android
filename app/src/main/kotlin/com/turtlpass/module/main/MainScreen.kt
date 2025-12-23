package com.turtlpass.module.main

import android.app.ActivityOptions
import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.R
import com.turtlpass.appmanager.viewmodel.AppManagerUiState
import com.turtlpass.model.WebsiteUi
import com.turtlpass.module.main.navigation.NavigationScreens
import com.turtlpass.module.main.ui.bottombar.BottomNavigationBar
import com.turtlpass.module.main.ui.topbar.TopAppBarMain
import com.turtlpass.module.selection.SelectionActivity
import com.turtlpass.module.selection.model.SelectedApp
import com.turtlpass.module.selection.model.SelectedWebDomain
import com.turtlpass.ui.bottomSheet.rememberBottomSheetNavigator
import com.turtlpass.ui.statusBar.StatusBarColors
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.urlmanager.viewmodel.UrlManagerUiState
import com.turtlpass.usb.model.UsbDeviceUiState
import com.turtlpass.usb.model.UsbUiState
import com.turtlpass.usb.ui.UsbDeviceStateView
import com.turtlpass.usb.ui.rememberStripeBrush
import com.turtlpass.useraccount.model.UserAccount
import com.turtlpass.useraccount.viewmodel.UserAccountUiState
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalTextApi::class,
    ExperimentalCoroutinesApi::class,
    FlowPreview::class,
)
@Composable
fun MainScreen(
    userAccountUiState: State<UserAccountUiState>,
    appManagerUiState: State<AppManagerUiState>,
    onAppSearch: (query: String) -> Unit,
    urlManagerUiState: State<UrlManagerUiState>,
    usbUiState: State<UsbUiState>,
    onUsbRequestPermissionClick: () -> Unit,
    navController: NavHostController,
    onAccountPickerRequested: () -> Unit,
    onUserAccount: (account: UserAccount) -> Unit,
    onDeleteWebsite: (websiteUi: WebsiteUi) -> Unit,
    onClearAllWebsites: () -> Unit,
    finish: () -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val hazeState = rememberHazeState()
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState,
        snapAnimationSpec = spring(stiffness = Spring.StiffnessHigh)
    )
    val statusBarBrush = rememberStripeBrush(
        state = usbUiState.value.usbDeviceUiState
    )
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val bottomSheetNavigator = rememberBottomSheetNavigator(
        sheetState = sheetState,
        skipHalfExpanded = true,
    )
    navController.navigatorProvider += bottomSheetNavigator

    val popBackStack: (String?) -> Unit = { route ->
        if (route != null) {
            navController.popBackStack(route = route, inclusive = false)
        } else {
            navController.popBackStack()
        }
    }
    var isSheetOpened by remember { mutableStateOf(false) }
    BackHandler {
        if (isSheetOpened) {
            popBackStack(null)
        } else {
            finish()
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
            }
        }
    }

    ModalBottomSheetLayout(
        modifier = Modifier,
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(
            topStart = dimensions.x16,
            topEnd = dimensions.x16
        ),
        sheetBackgroundColor = colors.default.sheetBackground,
        scrimColor = colors.default.scrim,
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                Column {
                    Column(
                        modifier = Modifier.background(statusBarBrush)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .windowInsetsTopHeight(WindowInsets.statusBars)
                        )
                        UsbDeviceStateView(
                            usbDeviceUiState = usbUiState.value.usbDeviceUiState,
                            onUsbRequestPermissionClick = onUsbRequestPermissionClick
                        )
                    }
                    TopAppBarMain(
                        hazeState = hazeState,
                        scrollBehavior = scrollBehavior,
                        userAccountUiState = userAccountUiState,
                        onAccountPickerRequested = onAccountPickerRequested
                    )
                }
            },
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    hazeState = hazeState
                )
            },
            containerColor = colors.default.background,
        ) { innerPadding ->
            StatusBarColors(darkIcons = false)
            Box(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                NavigationScreens(
                    hazeState = hazeState,
                    topAppBarState = topAppBarState,
                    userAccountUiState = userAccountUiState,
                    appManagerUiState = appManagerUiState,
                    appSearchEnabled = usbUiState.value.isUsbConnected.not(),
                    onAppSearch = onAppSearch,
                    urlManagerUiState = urlManagerUiState,
                    navController = navController,
                    onUserSelected = { account ->
                        onUserAccount(account)
                    },
                    onAppSelected = { app ->
                        context.startActivity(
                            Intent(context, SelectionActivity::class.java).apply {
                                putExtra(
                                    SelectionActivity.EXTRA_SELECTED_APP, SelectedApp(
                                        appName = app.appName,
                                        packageName = app.packageName,
                                        topLevelDomain = app.topLevelDomain,
                                    )
                                )
                            },
                            ActivityOptions.makeCustomAnimation(
                                context, R.anim.fade_in_fast, R.anim.fade_out_fast
                            ).toBundle()
                        )
                    },
                    onSelectWebsite = { websiteUi ->
                        context.startActivity(
                            Intent(context, SelectionActivity::class.java).apply {
                                putExtra(
                                    SelectionActivity.EXTRA_WEB_DOMAIN, SelectedWebDomain(
                                        url = websiteUi.url,
                                        topLevelDomain = websiteUi.topLevelDomain,
                                        packageName = websiteUi.packageName,
                                        faviconUrl = websiteUi.faviconUrl,
                                    )
                                )
                            },
                            ActivityOptions.makeCustomAnimation(
                                context, R.anim.fade_in_fast, R.anim.fade_out_fast
                            ).toBundle()
                        )
                    },
                    onDeleteWebsite = onDeleteWebsite,
                    onClearAllWebsites = onClearAllWebsites,
                    popBackStack = popBackStack,
                )
            }
        }
    }
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
    backgroundColor = 0xff303030,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Composable
private fun Preview() {
    AppTheme {
        MainScreen(
            navController = rememberNavController(),
            userAccountUiState = remember { mutableStateOf(UserAccountUiState()) },
            appManagerUiState = remember { mutableStateOf(AppManagerUiState()) },
            onAppSearch = {},
            urlManagerUiState = remember {
                mutableStateOf(
                    UrlManagerUiState(
                        isAccessibilityEnabled = true
                    )
                )
            },
            usbUiState = remember {
                mutableStateOf(
                    UsbUiState(usbDeviceUiState = UsbDeviceUiState.Attached)
                )
            },
            onUsbRequestPermissionClick = {},
            onAccountPickerRequested = {},
            onUserAccount = {},
            onDeleteWebsite = {},
            finish = {},
            onClearAllWebsites = {},
        )
    }
}
