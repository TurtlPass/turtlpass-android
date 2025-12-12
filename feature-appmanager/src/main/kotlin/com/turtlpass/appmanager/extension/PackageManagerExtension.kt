package com.turtlpass.appmanager.extension

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

@SuppressLint("QueryPermissionsNeeded")
fun PackageManager.getInstalledApplicationsCompat(): List<ApplicationInfo> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0L))
    } else @Suppress("DEPRECATION") getInstalledApplications(0)
}

@SuppressLint("QueryPermissionsNeeded")
fun PackageManager.getInstalledPackagesCompat(): List<PackageInfo> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getInstalledPackages(PackageManager.PackageInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
    } else @Suppress("DEPRECATION") getInstalledPackages(PackageManager.GET_META_DATA)
}

fun PackageManager.getPackageInfoCompat(packageName: String, flags: Int = 0): PackageInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
    } else @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
}
