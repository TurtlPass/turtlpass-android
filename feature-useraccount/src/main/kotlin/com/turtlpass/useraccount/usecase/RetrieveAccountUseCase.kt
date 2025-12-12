package com.turtlpass.useraccount.usecase

import com.turtlpass.di.IoDispatcher
import com.turtlpass.useraccount.model.UserAccount
import com.turtlpass.useraccount.repository.AccountIdRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RetrieveAccountUseCase @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val repository: AccountIdRepository,
    private val resolveUserAccountUseCase: ResolveUserAccountUseCase,
    ) {
    operator fun invoke(): Flow<UserAccount?> {
        return repository.retrieveAccountId()
            .map { accountId ->
                accountId?.let { resolveUserAccountUseCase(it) }
            }
            .catch {
                emit(null)
            }
            .flowOn(dispatcher)
    }
}
