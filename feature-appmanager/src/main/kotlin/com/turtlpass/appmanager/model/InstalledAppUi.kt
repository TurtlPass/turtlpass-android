package com.turtlpass.appmanager.model

import androidx.compose.runtime.Immutable
import com.turtlpass.model.InstalledApp

@Immutable
data class InstalledAppUi(
    val appName: String,
    val packageName: String,
    val topLevelDomain: String = "",
)

fun InstalledApp.toUi(): InstalledAppUi =
    InstalledAppUi(
        appName = appName,
        packageName = packageName,
        topLevelDomain = topLevelDomain
    )
