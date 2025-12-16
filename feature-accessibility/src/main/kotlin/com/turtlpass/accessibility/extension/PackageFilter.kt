package com.turtlpass.accessibility.extension

fun isLauncherApp(packageName: String): Boolean =
    packageName == "com.android.systemui"
            || packageName.lowercase().contains("launcher")
