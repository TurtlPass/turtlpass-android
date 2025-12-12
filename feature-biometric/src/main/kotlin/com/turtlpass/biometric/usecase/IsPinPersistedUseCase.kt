package com.turtlpass.biometric.usecase

import com.turtlpass.biometric.repository.PinRepository
import com.turtlpass.di.IoDispatcher
import com.turtlpass.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class IsPinPersistedUseCase @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val pinRepository: PinRepository,
) {
    operator fun invoke(): Flow<Result<Boolean>> {
        return flow {
            if (pinRepository.isPinPresent()) {
                emit(Result.Success(true))
            } else {
                emit(Result.Error())
            }
        }.catch {
            emit(Result.Error(it))
        }.flowOn(dispatcher)
    }
}
