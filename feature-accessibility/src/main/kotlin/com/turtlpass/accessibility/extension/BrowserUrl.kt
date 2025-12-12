package com.turtlpass.accessibility.extension

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.turtlpass.accessibility.model.BrowserConfig

fun getBrowserUrlString(info: AccessibilityNodeInfo, urlBarId: String): String? {
    val nodeList = info.findAccessibilityNodeInfosByViewId(urlBarId)
    if (nodeList.isNullOrEmpty()) return null
    return nodeList.getOrNull(0)?.text?.toString()
}

fun getBrowserUrlViewId(event: AccessibilityEvent): String? {
    val packageName = event.packageName?.toString() ?: return null
    return supportedBrowsers[packageName]?.urlBarId
}

fun isBrowserApp(packageName: String): Boolean {
    return supportedBrowsers.containsKey(packageName)
}

val supportedBrowsers = mapOf(
    "com.android.chrome" to BrowserConfig(
        "com.android.chrome",
        "com.android.chrome:id/url_bar"
    ),
    "org.chromium.chrome" to BrowserConfig(
        "org.chromium.chrome",
        "org.chromium.chrome:id/url_bar"
    ),
    "org.mozilla.firefox" to BrowserConfig(
        "org.mozilla.firefox",
        "org.mozilla.firefox:id/mozac_browser_toolbar_url_view"
    ),
    "com.opera.browser" to BrowserConfig(
        "com.opera.browser",
        "com.opera.browser:id/url_field"
    ),
    "com.opera.mini.native" to BrowserConfig(
        "com.opera.mini.native",
        "com.opera.mini.native:id/url_field"
    ),
    "com.duckduckgo.mobile.android" to BrowserConfig(
        "com.duckduckgo.mobile.android",
        "com.duckduckgo.mobile.android:id/omnibarTextInput"
    ),
    "com.microsoft.emmx" to BrowserConfig(
        "com.microsoft.emmx",
        "com.microsoft.emmx:id/url_bar"
    )
)
