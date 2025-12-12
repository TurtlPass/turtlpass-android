package com.turtlpass.accessibility.extension

fun shouldSendThisPackageName(packageName: String, className: String): Boolean =
    (packageName == "com.android.systemui"
            || packageName.lowercase().contains("launcher")
            || className.lowercase().contains("launcher")).not()
