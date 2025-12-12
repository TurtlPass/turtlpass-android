package com.turtlpass.useraccount.repository

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
    fun deviceRegisteredAccountEmails(): List<Account> =
        accountManager.accounts
            .filter { PatternsCompat.EMAIL_ADDRESS.matcher(it.name).matches() }
            .distinctBy { it.name }
            .sortedWith(
                compareBy<Account>(
                    { it.priority() },
                    { it.name }
                )
            )

    private fun Account.priority(): Int = when (type) {
        "com.google" -> 0
        "com.microsoft" -> 1
        else -> 2
    }
}
