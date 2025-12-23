package com.turtlpass.appmanager.usecase

import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.domain.Result
import kotlinx.coroutines.flow.Flow

interface InstalledAppsUseCase {
    operator fun invoke(): Flow<Result<List<InstalledAppUi>>>
}
