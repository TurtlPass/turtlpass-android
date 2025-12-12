package com.turtlpass.useraccount.viewmodel

import com.turtlpass.domain.Result
import com.turtlpass.useraccount.model.UserAccount

data class UserAccountUiState(
    val userAccountsResult: Result<List<UserAccount>> = Result.Error(),
    val selectedAccount: UserAccount? = null,
    val accountsPermission: AccountsPermission = AccountsPermission.Unknown,
)
