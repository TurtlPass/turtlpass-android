package com.turtlpass.urlmanager.usecase

import com.turtlpass.domain.Result
import com.turtlpass.model.WebsiteUi
import kotlinx.coroutines.flow.Flow

interface WebsiteUpdatesUseCase {
    operator fun invoke(maxEvents: Int = 100): Flow<Result<List<WebsiteUi>>>
}
