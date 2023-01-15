package com.turtlpass.module.useraccount.repo

import android.accounts.Account
import android.accounts.AccountManager
import androidx.core.util.PatternsCompat
import javax.inject.Inject

class DeviceAccountsRepository @Inject constructor(
    private val accountManager: AccountManager,
) {
    /**
     * Lists all accounts registered on the device using an email address as name
     */
    fun deviceRegisteredAccountEmails(): List<Account> {
        return accountManager.accounts
            .toList()
            .filterNotNull()
            .filter { account -> PatternsCompat.EMAIL_ADDRESS.matcher(account.name).matches() }
            .distinctBy { account -> account.name }
            .sortedBy { account -> account.name }
    }
}
