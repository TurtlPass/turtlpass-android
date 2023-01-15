package com.turtlpass.module.useraccount.repo

import android.content.SharedPreferences
import javax.inject.Inject

class AccountIdRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    /**
     * Persist account id for a given package name
     */
    fun persistAccountId(packageName: String, accountId: String) {
        sharedPreferences.edit().putString(packageName, accountId).apply()
    }

    /**
     * Retrieve account id for a given package name
     */
    fun retrieveAccountId(packageName: String): String? {
        return sharedPreferences.getString(packageName, null)
    }
}
