package com.turtlpass.module.biometric.usecase

import com.turtlpass.common.domain.Result
import com.turtlpass.di.IoDispatcher
import com.turtlpass.module.passphrase.repo.PassphraseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class IsPassphraseEnabledUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val passphraseRepository: PassphraseRepository,
) {
    operator fun invoke(): Flow<Result<Boolean>> {
        return flow {
            if (passphraseRepository.isPassphrasePresent()) {
                emit(Result.Success(true))
            } else {
                emit(Result.Error())
            }
        }.catch {
            emit(Result.Error(it))
        }.flowOn(dispatcher)
    }
}
