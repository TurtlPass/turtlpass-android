package com.turtlpass.appmanager.viewmodel

import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.domain.Result

data class AppManagerUiState(
    val installedAppsResult: Result<List<InstalledAppUi>> = Result.Loading,
    val filteredApps: List<InstalledAppUi> = emptyList(),
    val recentAppsResult: Result<List<InstalledAppUi>> = Result.Loading,
    val isUsageAccessGranted: Boolean = true,
)
