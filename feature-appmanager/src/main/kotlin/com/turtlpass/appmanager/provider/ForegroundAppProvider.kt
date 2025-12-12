//package com.turtlpass.appmanager.provider
//
//import com.turtlpass.accessibility.bus.AccessibilityEventBus
//import jakarta.inject.Inject
//import jakarta.inject.Singleton
//
//@Singleton
//class ForegroundAppProvider @Inject constructor(
//    private val bus: AccessibilityEventBus,
//    private val recentAppsUsageProvider: RecentAppsUsageProvider
//) {
//
//    @Volatile
//    private var lastForegroundPackage: String? = null
//
//    @Volatile
//    private var lastCheckedTime: Long = 0L
//
//    private val debounceMs = 300L // debounce for rapid app switches
//
//    /**
//     * Returns the current foreground app package.
//     * Debounces rapid foreground changes to avoid flicker.
//     */
//    fun getCurrentForegroundApp(): String? {
//        val now = System.currentTimeMillis()
//
//        if (now - lastCheckedTime < debounceMs) {
//            return lastForegroundPackage
//        }
//
//        val current = bus.lastForegroundApp.value?.packageName
//            ?: recentAppsUsageProvider.getMostRecentApp()?.packageName
//
//        lastForegroundPackage = current
//        lastCheckedTime = now
//
//        return current
//    }
//}
