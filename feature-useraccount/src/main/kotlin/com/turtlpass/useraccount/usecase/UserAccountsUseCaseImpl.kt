package com.turtlpass.useraccount.usecase

import com.turtlpass.di.IoDispatcher
import com.turtlpass.domain.Result
import com.turtlpass.useraccount.model.UserAccount
import com.turtlpass.useraccount.repository.DeviceAccountsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserAccountsUseCaseImpl @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val accountRepository: DeviceAccountsRepository,
    private val resolveUserAccountUseCase: ResolveUserAccountUseCase,
) : UserAccountsUseCase {
    override operator fun invoke(): Flow<Result<List<UserAccount>>> {
        return flow {
            emit(Result.Loading)
            try {
                val accountList = accountRepository.deviceRegisteredAccountEmails()
                    .map { account -> resolveUserAccountUseCase(account.name) }

                if (accountList.isNotEmpty()) {
                    emit(Result.Success(accountList))
                } else {
                    emit(Result.Error())
                }
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.catch {
            emit(Result.Error(it))
        }.flowOn(dispatcher)
    }
}
