package com.turtlpass.appmanager.viewmodel

import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.domain.Result

data class AppManagerUiState(
    val installedAppsResult: Result<List<InstalledAppUi>> = Result.Loading,
    val recentAppsResult: Result<List<InstalledAppUi>> = Result.Loading,
    val isUsageAccessGranted: Boolean = false,
)
