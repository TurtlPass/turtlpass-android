package com.turtlpass.usb.intent

import androidx.lifecycle.ViewModel
import com.turtlpass.accessibility.bus.AccessibilityEventBus
import com.turtlpass.accessibility.extension.isLauncherApp
import com.turtlpass.model.ObservedAccessibilityEvent
import com.turtlpass.usb.state.UsbStateProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class UsbIntentViewModel @Inject constructor(
    private val accessibilityEventBus: AccessibilityEventBus,
    private val usbStateProvider: UsbStateProvider,
) : ViewModel() {

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
            }

        return when {
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
