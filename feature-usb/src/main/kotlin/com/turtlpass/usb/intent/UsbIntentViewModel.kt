package com.turtlpass.usb.intent

import androidx.lifecycle.ViewModel
import com.turtlpass.accessibility.bus.AccessibilityEventBus
import com.turtlpass.model.ObservedAccessibilityEvent
import com.turtlpass.usb.state.UsbStateProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class UsbIntentViewModel @Inject constructor(
//    private val foregroundAppProvider: ForegroundAppProvider,
    private val accessibilityEventBus: AccessibilityEventBus,
    private val usbStateProvider: UsbStateProvider,
) : ViewModel() {

    // Debounce for rapid calls
    private val debounceMs = 300L

    @Volatile
    private var lastDecisionTime: Long = 0L

    // Cooldown for repeated USB events
    private val cooldownMs = 1000L

    @Volatile
    private var lastActionTime: Long = 0L


    /**
     * Determines what to do for the USB intent.
     * Applies debounce and cooldown to prevent rapid repeated triggers.
     */
    fun decideUsbIntent(): UsbIntentDecision {
        val now = System.currentTimeMillis()

        // Debounce: ignore repeated calls within [debounceMs]
        if (now - lastDecisionTime < debounceMs) {
            return UsbIntentDecision.Finish
        }
        lastDecisionTime = now

        // Cooldown: prevent repeating overlay/main launches within [cooldownMs]
        if (now - lastActionTime < cooldownMs) {
            return UsbIntentDecision.Finish
        }

        // Only launch overlay if USB is connected **and permission granted**
        val usbConnected = usbStateProvider.isUsbConnected()
        val usbPermissionGranted = usbStateProvider.hasUsbPermission()

//        if (!usbConnected || !usbPermissionGranted) {
//            return UsbIntentDecision.LaunchMain
//        }

        // Wait for permission, don't launch anything yet
        if (!usbPermissionGranted) {
            return UsbIntentDecision.Finish
        }

//        if (usbConnected && !usbPermissionGranted || !usbConnected) {
//            return UsbIntentDecision.Finish
//        }

//        if (!usbConnected) {
//            return UsbIntentDecision.LaunchMain
//        }

        val lastEvent = accessibilityEventBus.lastEvent.value
        val foregroundPackage = lastEvent
            ?.let {
                when (it) {
                    is ObservedAccessibilityEvent.AppEvent -> it.packageName
                    is ObservedAccessibilityEvent.UrlEvent -> it.packageName
                }
            }
//            ?: foregroundAppProvider.getCurrentForegroundApp()

//        val foregroundPackage = foregroundAppProvider.getCurrentForegroundApp()

        val ourPackage = "com.turtlpass"

        val decision = when {
            foregroundPackage == null -> UsbIntentDecision.LaunchMain
            foregroundPackage == ourPackage -> UsbIntentDecision.Finish
            else -> UsbIntentDecision.LaunchSelection
        }

        // Only count action time if we are actually launching something
        if (decision != UsbIntentDecision.Finish) {
            lastActionTime = now
        }

        return decision
    }
}