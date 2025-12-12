package com.turtlpass.appmanager.usecase

import android.content.pm.PackageManager
import com.turtlpass.appmanager.extension.getPackageInfoCompat
import timber.log.Timber
import javax.inject.Inject

class GetAppNameUseCase @Inject constructor(
    private val packageManager: PackageManager,
) {
    operator fun invoke(packageName: String): String? =
        runCatching {
            packageManager
                .getPackageInfoCompat(packageName, PackageManager.GET_META_DATA)
                .applicationInfo
                ?.let { appInfo -> packageManager.getApplicationLabel(appInfo).toString() }
        }.onFailure { e ->
            Timber.e(e, "Failed to get app name for package: $packageName")
        }.getOrNull()
}
