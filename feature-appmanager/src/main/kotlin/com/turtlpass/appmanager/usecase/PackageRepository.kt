package com.turtlpass.appmanager.usecase

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.turtlpass.appmanager.extension.getInstalledPackagesCompat
import javax.inject.Inject

class PackageRepository @Inject constructor(
    private val packageManager: PackageManager,
) {
    fun installedPackagesList(): List<PackageInfo> {
        return packageManager.getInstalledPackagesCompat()
    }
}
