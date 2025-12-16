package com.turtlpass.accessibility.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import com.turtlpass.accessibility.bus.AccessibilityEventBus
import com.turtlpass.accessibility.extension.getBrowserUrlString
import com.turtlpass.accessibility.extension.getBrowserUrlViewId
import com.turtlpass.accessibility.extension.isBrowserApp
import com.turtlpass.accessibility.extension.parseUrl
import com.turtlpass.db.dao.WebsiteEventDao
import com.turtlpass.db.entities.WebsiteEventEntity
import com.turtlpass.model.ObservedAccessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Command to disable battery optimization via adb
 * $ adb shell am set-standby-bucket com.turtlpass exempt
 * $ adb shell am get-standby-bucket com.turtlpass
 * or/and
 * $ adb shell am set-inactive com.turtlpass false
 * $ adb shell dumpsys deviceidle whitelist +com.turtlpass
 * $ adb shell dumpsys deviceidle whitelist
 * or/and
 * $ adb shell dumpsys deviceidle disable com.turtlpass
 *
 *
 * Enable Accessibility Services of this app via adb
 * $ adb shell settings put secure enabled_accessibility_services com.turtlpass/.accessibility.service.PersistentAccessibilityService
 */
@SuppressLint("AccessibilityPolicy")
@AndroidEntryPoint
class PersistentAccessibilityService : AccessibilityService() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    @Inject
    lateinit var bus: AccessibilityEventBus
    @Inject
    lateinit var dao: WebsiteEventDao
    private var inCooldown = false
    private var lastDomain: String? = null
    private var lastPackageName: String? = null
    private val handler = Handler(Looper.getMainLooper())


    public override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = AccessibilityServiceInfo().apply {
            flags = AccessibilityServiceInfo.DEFAULT or
                    AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
                    AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                    AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || event.source == null || event.packageName == null || event.className == null) return
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (!isBrowserApp(packageName)) {
                processForegroundApp(event)
            }
        } else if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            if (!inCooldown) {
                processBrowserSnapshot(event)
            }
        }
    }

    private fun processForegroundApp(event: AccessibilityEvent) {
        val packageName = event.packageName?.toString() ?: return
        // Skip if packageName hasn't changed
        if (packageName == lastPackageName) return
        lastPackageName = packageName
        bus.tryEmit(
            ObservedAccessibilityEvent.AppEvent(
                packageName = packageName,
                timestamp = System.currentTimeMillis() - SystemClock.uptimeMillis() + event.eventTime
            )
        )
    }

    private fun processBrowserSnapshot(event: AccessibilityEvent) {
        event.source?.let { root ->
            val packageName = event.packageName?.toString() ?: return
            if (!isBrowserApp(packageName)) return
            // Check first if it's a Custom Tab
            getBrowserUrlString(root, "$packageName:id/customTabDomain")?.let { urlString ->
                processBrowserUrl(urlString, packageName, event.eventTime)
                return
            }
            getBrowserUrlViewId(event)?.let { urlBarId ->
                getBrowserUrlString(root, urlBarId)?.let { urlString ->
                    processBrowserUrl(urlString, packageName, event.eventTime)
                }
            }
        }
    }

    private fun processBrowserUrl(urlString: String, packageName: String, eventTime: Long) {
        // Enter cooldown so we don't trigger immediately again
        enterCooldown()
        // Parse URL
        val domain = parseUrl(urlString) ?: return
        // Skip if domain hasn't changed
        if (domain == lastDomain) return
        lastDomain = domain
        // Trigger callback
        val timestamp = System.currentTimeMillis() - SystemClock.uptimeMillis() + eventTime
        onNewDomainDetected(domain, packageName, timestamp)
    }

    private fun enterCooldown() {
        inCooldown = true
        handler.postDelayed({
            inCooldown = false
        }, 1000) // cooldown duration
    }

    private fun onNewDomainDetected(domain: String, packageName: String, timestamp: Long) {
        bus.tryEmit(
            ObservedAccessibilityEvent.UrlEvent(
                packageName = packageName,
                url = domain,
                timestamp = timestamp
            )
        )
        // Launch coroutine to write asynchronously
        scope.launch {
            dao.insert(
                WebsiteEventEntity(
                    url = domain,
                    packageName = packageName,
                    timestamp = timestamp
                )
            )
        }
    }

    override fun onInterrupt() {
        // do nothing
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        inCooldown = false
        lastDomain = null
        lastPackageName = null
        scope.cancel()
    }
}
