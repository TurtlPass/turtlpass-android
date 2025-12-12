package com.turtlpass.module.selection.viewmodel

import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.accessibility.bus.AccessibilityEventBus
import com.turtlpass.accessibility.extension.isBrowserApp
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.appmanager.provider.RecentAppsUsageProvider
import com.turtlpass.appmanager.usecase.BuildInstalledAppUseCase
import com.turtlpass.appmanager.usecase.UsageAccessUseCase
import com.turtlpass.domain.parcelable
import com.turtlpass.model.ObservedAccessibilityEvent
import com.turtlpass.model.WebsiteUi
import com.turtlpass.module.selection.SelectionActivity
import com.turtlpass.module.selection.deviceFlow.DeviceFlowMode
import com.turtlpass.module.selection.model.ProductTypeUi
import com.turtlpass.module.selection.model.SelectedApp
import com.turtlpass.module.selection.model.SelectedWebDomain
import com.turtlpass.module.selection.model.SelectionInput
import com.turtlpass.module.selection.model.SelectionUiState
import com.turtlpass.urlmanager.extension.faviconUrl
import com.turtlpass.urlmanager.usecase.BuildWebsiteUiUseCase
import com.turtlpass.urlmanager.usecase.UpdateWebsiteUiUseCase
import com.turtlpass.usb.model.UsbUiState
import com.turtlpass.usb.state.UsbStateProvider
import com.turtlpass.usb.ui.LoaderType
import com.turtlpass.usb.viewmodel.UsbUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(
    ExperimentalCoroutinesApi::class,
    ExperimentalPermissionsApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalTextApi::class,
    FlowPreview::class
)
@HiltViewModel
class SelectionViewModel @Inject constructor(
    private val accessibilityEventBus: AccessibilityEventBus,
    private val recentAppsUsageProvider: RecentAppsUsageProvider,
    private val usageAccessUseCase: UsageAccessUseCase,
    private val buildInstalledAppUseCase: BuildInstalledAppUseCase,
    private val buildWebsiteUiUseCase: BuildWebsiteUiUseCase,
    private val updateWebsiteUiUseCase: UpdateWebsiteUiUseCase,
    usbStateProvider: UsbStateProvider,
) : ViewModel() {

    private val _uiState: MutableStateFlow<SelectionUiState> by lazy {
        MutableStateFlow(SelectionUiState(model = SelectionInput()))
    }
    val uiState: StateFlow<SelectionUiState> by lazy { _uiState.asStateFlow() }

    val usbUiState: StateFlow<UsbUiState> = usbStateProvider.usbUiState


    fun handleIntent(intent: Intent) {
        if (intent.getBooleanExtra(SelectionActivity.EXTRA_IS_USB_INTENT, false)) {
            _uiState.update { state -> state.copy(flowMode = DeviceFlowMode.BottomSheet) }
            handleUsbIntent()

        } else {
            // Normal non-USB intent logic

            intent.getStringExtra(SelectionActivity.EXTRA_SHARE_URL)?.let { url ->
                _uiState.update { state -> state.copy(flowMode = DeviceFlowMode.BottomSheet) }
                selectProductType(ProductTypeUi.Website)
                updateBrowserState(url)
            }

            intent.parcelable<SelectedWebDomain>(SelectionActivity.EXTRA_WEB_DOMAIN)
                ?.let { selectedWebDomain ->
                    _uiState.update { state -> state.copy(flowMode = DeviceFlowMode.FullScreen) }

                    selectProductType(ProductTypeUi.Website)
                    updateBrowserState(
                        WebsiteUi(
                            packageName = selectedWebDomain.packageName,
                            url = selectedWebDomain.url,
                            topLevelDomain = selectedWebDomain.topLevelDomain,
                            faviconUrl = selectedWebDomain.faviconUrl,
                        )
                    )
                }

            intent.parcelable<SelectedApp>(SelectionActivity.EXTRA_SELECTED_APP)
                ?.let { selectedApp ->
                    _uiState.update { state -> state.copy(flowMode = DeviceFlowMode.FullScreen) }

                    selectProductType(ProductTypeUi.Application)
                    updateInstalledApp(
                        InstalledAppUi(
                            appName = selectedApp.appName,
                            packageName = selectedApp.packageName,
                            topLevelDomain = selectedApp.topLevelDomain
                        )
                    )
                }
        }
    }

    private fun SelectionViewModel.handleUsbIntent() {
        when (val lastEvent = accessibilityEventBus.lastEvent.value) {
            is ObservedAccessibilityEvent.AppEvent -> {
                // Last event was an app
                when {
//                    lastEvent.packageName == "com.turtlpass" -> {
////                        finish()
//                    }
                    isBrowserApp(lastEvent.packageName) -> {
                        accessibilityEventBus.lastUrl.value?.let { lastUrl ->
                            if (lastUrl.packageName == lastEvent.packageName) {
                                selectProductType(ProductTypeUi.Website)
                                updateBrowserState(lastUrl)
                            }//todo else finish()
                        }//todo else finish()
                    }

                    else -> {
                        selectProductType(ProductTypeUi.Application)
                        updateInstalledApp(lastEvent.packageName)
                    }
                }
            }

            is ObservedAccessibilityEvent.UrlEvent -> {
                // Last event was a web domain
                selectProductType(ProductTypeUi.Website)
                updateBrowserState(lastEvent)
            }

            null -> {
                // Fallback if no accessibility event yet
                handleUsbIntentFallback()
            }
        }
    }

    private fun SelectionViewModel.handleUsbIntentFallback() {
//        val foregroundPackageName =
//            intent.getStringExtra(SelectionActivity.EXTRA_PACKAGE_NAME)
//        if (!foregroundPackageName.isNullOrEmpty()) {
//            selectProductType(ProductTypeUi.Application)
//            updateInstalledApp(foregroundPackageName)
//        } else {
//
//        }
        recentAppsUsageProvider.getMostRecentApp()?.packageName?.let { packageName ->
            selectProductType(ProductTypeUi.Application)
            updateInstalledApp(packageName)
//            appManagerViewModel.updateInstalledApp(packageName)
        }
    }

    fun selectProductType(productTypeUi: ProductTypeUi) {
        _uiState.update { state ->
            state.copy(model = state.model.copy(productType = productTypeUi))
        }
    }

    fun updateInstalledApp(app: InstalledAppUi) {
        _uiState.update { state -> state.copy(model = state.model.copy(selectedApp = app)) }
    }

    fun updateInstalledApp(packageName: String) {
        buildInstalledAppUseCase(packageName)?.let { app ->
            _uiState.update { state -> state.copy(model = state.model.copy(selectedApp = app)) }
        }
    }

    fun updateBrowserState(websiteUi: WebsiteUi) {
        _uiState.update { state -> state.copy(model = state.model.copy(selectedUrl = websiteUi)) }
    }

    fun updateBrowserState(event: ObservedAccessibilityEvent.UrlEvent) {
        updateWebsiteUiUseCase(event).let { website ->
            _uiState.update { state -> state.copy(model = state.model.copy(selectedUrl = website)) }
        }
    }

    fun updateBrowserState(url: String) {
        buildWebsiteUiUseCase(url).let { website ->
            _uiState.update { state -> state.copy(model = state.model.copy(selectedUrl = website)) }
        }
    }

    fun updatePin(pin: String) {
        _uiState.update { state ->
            state.copy(model = state.model.copy(selectedPin = pin))
        }
    }

    fun updateUsbUiEvent(usbEvent: UsbUiEvent) {
        when (usbEvent) {
            UsbUiEvent.UsbWriteLoading -> {
                _uiState.update { state ->
                    state.copy(loaderType = LoaderType.Loading)
                }
            }

            UsbUiEvent.UsbWriteSuccess -> {
                _uiState.update { state ->
                    state.copy(
                        loaderType = LoaderType.Success,
                        model = SelectionInput(),
                    )
                }
            }

            UsbUiEvent.UsbWriteError -> {
                _uiState.update { state ->
                    state.copy(loaderType = LoaderType.Error)
                }
            }
        }
    }
}
