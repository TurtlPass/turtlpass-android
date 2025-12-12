package com.turtlpass.appmanager.usecase

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.turtlpass.model.InstalledApp
import javax.inject.Inject

class RetrieveInstalledAppUseCase @Inject constructor(
    private val packageManager: PackageManager,
    private val extractAppTopLevelDomainUseCase: ExtractAppTopLevelDomainUseCase,
) {
    operator fun invoke(appInfo: ApplicationInfo): InstalledApp? {
        val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        val isAndroidPackage = appInfo.packageName.startsWith("com.android.")
        val hasLaunchIntent = packageManager.getLaunchIntentForPackage(appInfo.packageName) != null

        // Skip system, internal, or non-launchable apps
        if (isSystemApp || isAndroidPackage || !hasLaunchIntent) {
            return null
        }

        return InstalledApp(
            appName = packageManager.getApplicationLabel(appInfo).toString(),
            packageName = appInfo.packageName,
            topLevelDomain = extractAppTopLevelDomainUseCase(appInfo.packageName)
        )
    }
}