package com.turtlpass.module.installedapp.model

data class InstalledApp(
    val appName: String,
    val packageName: String,
    val topLevelDomain: String = "",
)
