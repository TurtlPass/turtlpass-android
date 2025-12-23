package com.turtlpass.useraccount.usecase

import com.turtlpass.domain.Result
import com.turtlpass.useraccount.model.UserAccount
import kotlinx.coroutines.flow.Flow

interface UserAccountsUseCase {
    operator fun invoke(): Flow<Result<List<UserAccount>>>
}
