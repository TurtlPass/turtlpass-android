package com.turtlpass.appmanager.usecase

import com.turtlpass.di.IoDispatcher
import com.turtlpass.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MockUsageAccessUseCaseImpl @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
) : UsageAccessUseCase {
    override operator fun invoke(): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        emit(Result.Success(true))
    }.flowOn(dispatcher)
}
