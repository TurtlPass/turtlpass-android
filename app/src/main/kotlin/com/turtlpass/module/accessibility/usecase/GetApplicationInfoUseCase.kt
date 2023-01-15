package com.turtlpass.module.accessibility.usecase

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.turtlpass.common.extension.getPackageInfoCompat
import com.turtlpass.module.chooser.usecase.ExtractTopLevelDomainUseCase
import com.turtlpass.module.installedapp.model.InstalledApp
import timber.log.Timber
import javax.inject.Inject

class GetApplicationInfoUseCase @Inject constructor(
    private val packageManager: PackageManager,
    private val extractTopLevelDomainUseCase: ExtractTopLevelDomainUseCase,
) {
    operator fun invoke(packageName: String): InstalledApp? {
        try {
            val packageInfo =
                packageManager.getPackageInfoCompat(packageName, PackageManager.GET_META_DATA)
            val appInfo = packageInfo.applicationInfo

            if (appInfo.flags and (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP or ApplicationInfo.FLAG_SYSTEM) <= 0) {
                return InstalledApp(
                    appName = packageManager.getApplicationLabel(appInfo).toString(),
                    packageName = appInfo.packageName,
                    topLevelDomain = extractTopLevelDomainUseCase(appInfo.packageName)
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return null // it's a system app
    }
}
