package com.turtlpass.common.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

/**
 * Executes business logic in its execute method and keep posting updates to the result as [Result<R>].
 * Handling an exception (emit [Result.Error] to the result) is the subclasses responsibility.
 *
 * src: https://github.com/google/iosched/blob/main/shared/src/main/java/com/google/samples/apps/iosched/shared/domain/FlowUseCase.kt
 */
abstract class FlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    operator fun invoke(parameters: P): Flow<Result<R>> {
        return execute(parameters)
            .catch {
                Timber.e(it)
                emit(Result.Error(it))
            }
            .flowOn(coroutineDispatcher)
    }

    abstract fun execute(parameters: P): Flow<Result<R>>
}
