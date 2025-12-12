package com.turtlpass.useraccount.usecase

import com.turtlpass.di.IoDispatcher
import com.turtlpass.useraccount.model.UserAccount
import com.turtlpass.useraccount.repository.AccountIdRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersistAccountUseCase @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val repository: AccountIdRepository,
) {
    suspend operator fun invoke(account: UserAccount) = withContext(dispatcher) {
        repository.persistAccountId(account.accountId)
    }
}
