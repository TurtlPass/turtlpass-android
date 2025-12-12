package com.turtlpass.appmanager.usecase

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.turtlpass.appmanager.extension.getInstalledApplicationsCompat
import javax.inject.Inject

class DeviceAppsRepository @Inject constructor(
    private val packageManager: PackageManager,
) {
    fun deviceInstalledApplications(): List<ApplicationInfo> {
        return packageManager.getInstalledApplicationsCompat()
    }
}
