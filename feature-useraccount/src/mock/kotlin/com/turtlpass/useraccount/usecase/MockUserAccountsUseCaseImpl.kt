package com.turtlpass.useraccount.usecase

import com.turtlpass.domain.Result
import com.turtlpass.useraccount.model.UserAccount
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockUserAccountsUseCaseImpl @Inject constructor(
) : UserAccountsUseCase {
    override operator fun invoke(): Flow<Result<List<UserAccount>>> = flow {
        emit(Result.Loading)
        emit(
            Result.Success(
                listOf(
                    UserAccount(
                        "ryan@example.com",
                        avatarUrl = "https://s.gravatar.com/avatar/84c1c149c46b921d221f3febf47ad606?s=200&d=404"
                    ),
                    UserAccount(
                        "amaral@example.com",
                        avatarUrl = "https://s.gravatar.com/avatar/84c1c149c46b921d221f3febf47ad606?s=200&d=404"
                    ),
                )
            )
        )
    }
}
