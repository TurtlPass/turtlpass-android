package com.turtlpass.module.useraccount.repo

import android.content.SharedPreferences
import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AccountIdRepositoryTest {

    private lateinit var accountIdRepository: AccountIdRepository
    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setup() {
        sharedPreferences = SPMockBuilder().createSharedPreferences()
        accountIdRepository = AccountIdRepository(sharedPreferences)
    }

    @Test
    fun `Given account id and package, When persist called, Then account id is saved`() {
        val packageName = "com.example"
        val accountId = "mail@example.com"
        accountIdRepository.persistAccountId(packageName, accountId)
        val retrievedAccountId = accountIdRepository.retrieveAccountId(packageName)
        assertEquals(accountId, retrievedAccountId)
    }

    @Test
    fun `Given account id saved, When retrieve called with package, Then saved account id is returned`() {
        val packageName = "com.example"
        val accountId = "mail@example.com"
        accountIdRepository.persistAccountId(packageName, accountId)
        val retrievedAccountId = accountIdRepository.retrieveAccountId(packageName)
        assertEquals(accountId, retrievedAccountId)
    }
}
