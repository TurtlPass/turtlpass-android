package com.turtlpass.appmanager.usecase

import android.app.AppOpsManager
import android.content.Context
import android.os.Process
import android.provider.Settings

class UsagePermissionProvider(private val context: Context) : PermissionProvider {

    override fun checkDrawOverlay(): Boolean {
        return Settings.canDrawOverlays(context)
    }

    override fun checkUsageStats(): Boolean {
        val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
}
