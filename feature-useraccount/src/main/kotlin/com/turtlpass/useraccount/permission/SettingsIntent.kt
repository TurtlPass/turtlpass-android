package com.turtlpass.useraccount.permission

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.net.toUri

fun Context.openAppSettings() {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = ("package:$packageName").toUri()
        startActivity(this)
    }
}
