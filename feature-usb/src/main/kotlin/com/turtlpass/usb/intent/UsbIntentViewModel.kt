package com.turtlpass.usb.intent

import androidx.lifecycle.ViewModel
import com.turtlpass.accessibility.bus.AccessibilityEventBus
import com.turtlpass.accessibility.extension.isLauncherApp
import com.turtlpass.appmanager.provider.RecentAppsUsageProvider
import com.turtlpass.model.ObservedAccessibilityEvent
import com.turtlpass.usb.model.AppState
import com.turtlpass.usb.state.UsbStateProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class UsbIntentViewModel @Inject constructor(
    private val accessibilityEventBus: AccessibilityEventBus,
    private val usbStateProvider: UsbStateProvider,
    private val recentAppsUsageProvider: RecentAppsUsageProvider

) : ViewModel() {

    val isAppInForeground: StateFlow<Boolean> = AppState.isForeground

    /**
     * Determines what to do for the USB intent
     */
    fun decideUsbIntent(): UsbIntentDecision {
        val lastEvent = accessibilityEventBus.lastEvent.value
        val foregroundPackage = lastEvent
            ?.let {
                when (it) {
                    is ObservedAccessibilityEvent.AppEvent -> it.packageName
                    is ObservedAccessibilityEvent.UrlEvent -> it.packageName
                }
            } ?: recentAppsUsageProvider.getMostRecentApp()?.packageName

        return when {
            foregroundPackage == null && isAppInForeground.value -> {
                // in case no permission for both UsageStatsManager and Accessibility
                UsbIntentDecision.Finish
            }
            foregroundPackage == null || isLauncherApp(foregroundPackage) -> {
                UsbIntentDecision.LaunchMain
            }
            foregroundPackage == "com.turtlpass" -> {
                UsbIntentDecision.Finish
            }
            else -> {
                if (usbStateProvider.hasUsbPermission()) UsbIntentDecision.LaunchSelection
                else UsbIntentDecision.Finish
            }
        }
    }
}
