package com.turtlpass.module.accessibility.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.turtlpass.BuildConfig

/**
 * Enable Accessibility Services of this app via adb
 * $ adb shell settings put secure enabled_accessibility_services com.turtlpass/.module.accessibility.service.PackageAccessibilityService
 */
class PackageAccessibilityService : AccessibilityService() {

    public override fun onServiceConnected() {
        super.onServiceConnected()

        /**
         *  AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS flag can help to improve performance by
         *  only reporting the view IDs of the changed views, rather than the entire view hierarchy.
         */
        serviceInfo = AccessibilityServiceInfo().apply {
            flags = AccessibilityServiceInfo.DEFAULT or AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        }
    }

    override fun onAccessibilityEvent(accessibilityEvent: AccessibilityEvent) {
        if (accessibilityEvent.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val currentPackageName = accessibilityEvent.packageName
            val className = accessibilityEvent.className
            if (currentPackageName == null || currentPackageName == applicationContext.packageName) return
            if (shouldSendThisPackageName(currentPackageName.toString(), className.toString())) {
                sendBroadcast(Intent(ACTION_FOREGROUND_APPLICATION_CHANGED).apply {
                    putExtra(EXTRA_PACKAGE_NAME, currentPackageName)
                    putExtra(EXTRA_CLASS_NAME, className)
                }, ACCESSIBILITY_SERVICE_PERMISSION)
            }
        }
    }

    override fun onInterrupt() {
        // do nothing
    }

    private fun shouldSendThisPackageName(packageName: String, className: String): Boolean =
        (packageName == "com.android.systemui"
                || packageName.lowercase().contains("launcher")
                || className.lowercase().contains("launcher")).not()

    companion object {
        const val ACTION_FOREGROUND_APPLICATION_CHANGED =
            "${BuildConfig.APPLICATION_ID}.ACTION_FOREGROUND_APPLICATION_CHANGED"
        const val EXTRA_PACKAGE_NAME =
            "${BuildConfig.APPLICATION_ID}.EXTRA_PACKAGE_NAME"
        const val EXTRA_CLASS_NAME =
            "${BuildConfig.APPLICATION_ID}.EXTRA_CLASS_NAME"
        private const val ACCESSIBILITY_SERVICE_PERMISSION =
            "${BuildConfig.APPLICATION_ID}.ACCESSIBILITY_SERVICE_PERMISSION"
    }
}
