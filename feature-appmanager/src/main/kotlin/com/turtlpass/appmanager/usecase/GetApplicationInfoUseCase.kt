package com.turtlpass.appmanager.usecase

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.turtlpass.appmanager.extension.getPackageInfoCompat
import com.turtlpass.model.InstalledApp
import timber.log.Timber
import javax.inject.Inject

class GetApplicationInfoUseCase @Inject constructor(
    private val packageManager: PackageManager,
    private val extractAppTopLevelDomainUseCase: ExtractAppTopLevelDomainUseCase,
) {
    operator fun invoke(packageName: String): InstalledApp? {
        try {
            val packageInfo =
                packageManager.getPackageInfoCompat(packageName, PackageManager.GET_META_DATA)
            val appInfo = packageInfo.applicationInfo

            if (appInfo!!.flags and (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP or ApplicationInfo.FLAG_SYSTEM) <= 0) {
                return InstalledApp(
                    appName = packageManager.getApplicationLabel(appInfo).toString(),
                    packageName = appInfo.packageName,
                    topLevelDomain = extractAppTopLevelDomainUseCase(appInfo.packageName)
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return null // it's a system app
    }
}
