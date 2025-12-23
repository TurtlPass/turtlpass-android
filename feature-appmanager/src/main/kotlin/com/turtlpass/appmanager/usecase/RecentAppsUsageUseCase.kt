package com.turtlpass.appmanager.usecase

import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.domain.Result
import kotlinx.coroutines.flow.Flow

interface RecentAppsUsageUseCase {
    operator fun invoke(
        minUsageSeconds: Long = 0,
        limit: Int = 20,
        refreshIntervalMs: Long = 5000L,  // poll every 5 seconds
    ): Flow<Result<List<InstalledAppUi>>>
}
