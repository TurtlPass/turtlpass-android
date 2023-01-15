package com.turtlpass.module.useraccount.usecase

import com.turtlpass.di.IoDispatcher
import com.turtlpass.module.useraccount.model.UserAccount
import com.turtlpass.module.useraccount.repo.AccountIdRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RetrieveAccountUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val repository: AccountIdRepository,
) {
    operator fun invoke(packageName: String): Flow<UserAccount?> {
        return flow {
            repository.retrieveAccountId(packageName)?.let { accountId ->
                emit(UserAccount(accountId, null))
            } ?: emit(null)
        }.catch {
            emit(null)
        }.flowOn(dispatcher)
    }
}
