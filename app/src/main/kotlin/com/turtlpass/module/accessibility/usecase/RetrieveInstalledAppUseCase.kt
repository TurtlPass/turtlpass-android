package com.turtlpass.module.accessibility.usecase

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.turtlpass.module.chooser.usecase.ExtractTopLevelDomainUseCase
import com.turtlpass.module.installedapp.model.InstalledApp
import javax.inject.Inject

class RetrieveInstalledAppUseCase @Inject constructor(
    private val packageManager: PackageManager,
    private val extractTopLevelDomainUseCase: ExtractTopLevelDomainUseCase,
) {
    operator fun invoke(appInfo: ApplicationInfo): InstalledApp? {
        if (appInfo.flags and (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP or ApplicationInfo.FLAG_SYSTEM) <= 0) {
            return InstalledApp(
                appName = packageManager.getApplicationLabel(appInfo).toString(),
                packageName = appInfo.packageName,
                topLevelDomain = extractTopLevelDomainUseCase(appInfo.packageName)
            )
        }
        return null // it's a system app
    }
}
