package com.turtlpass.appmanager.provider

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import com.turtlpass.appmanager.usecase.RetrieveInstalledAppUseCase
import com.turtlpass.model.InstalledApp
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class RecentAppsUsageProvider @Inject constructor(
    private val usageStatsManager: UsageStatsManager,
    private val packageManager: PackageManager,
    private val retrieveInstalledAppUseCase: RetrieveInstalledAppUseCase,
    @param:ApplicationContext private val context: Context
) {
    /**
     * Returns the most recently used app (excluding this app) or null.
     */
    fun getMostRecentApp(minUsageSeconds: Long = 0): InstalledApp? {
        return try {
            val endTime = System.currentTimeMillis()
            val startTime = endTime - 1000L * 60 * 60 * 24 * 7 // last 7 days

            val stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startTime,
                endTime
            )

            if (stats.isNullOrEmpty()) return null

            val minUsageMillis = minUsageSeconds * 1000L

            stats
                .filter { it.lastTimeUsed > 0 }
                .filter { it.totalTimeInForeground >= minUsageMillis }
                .sortedByDescending { it.lastTimeUsed }
                .distinctBy { it.packageName }
                .mapNotNull { usage ->
                    try {
                        val appInfo = packageManager.getApplicationInfo(usage.packageName, 0)
                        retrieveInstalledAppUseCase(appInfo)
                    } catch (e: Exception) {
                        null
                    }
                }
                .firstOrNull() // most recent app
        } catch (e: Exception) {
            null
        }
    }
}
