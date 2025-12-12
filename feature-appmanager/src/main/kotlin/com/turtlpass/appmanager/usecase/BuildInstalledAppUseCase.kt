package com.turtlpass.appmanager.usecase

import android.content.pm.PackageManager
import com.turtlpass.appmanager.extension.getPackageInfoCompat
import com.turtlpass.appmanager.model.InstalledAppUi
import timber.log.Timber
import javax.inject.Inject

class BuildInstalledAppUseCase @Inject constructor(
    private val packageManager: PackageManager,
    private val extractAppTopLevelDomainUseCase: ExtractAppTopLevelDomainUseCase,
) {
    operator fun invoke(packageName: String): InstalledAppUi? {
        try {
            packageManager
                .getPackageInfoCompat(packageName, PackageManager.GET_META_DATA)
                .applicationInfo?.let { appInfo ->
                    return InstalledAppUi(
                        appName = packageManager.getApplicationLabel(appInfo).toString(),
                        packageName = appInfo.packageName,
                        topLevelDomain = extractAppTopLevelDomainUseCase(appInfo.packageName)
                    )
                }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return null
    }
}
