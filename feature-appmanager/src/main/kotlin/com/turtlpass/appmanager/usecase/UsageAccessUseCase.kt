package com.turtlpass.appmanager.usecase

import com.turtlpass.domain.Result
import kotlinx.coroutines.flow.Flow

interface UsageAccessUseCase {
    operator fun invoke(): Flow<Result<Boolean>>
}
