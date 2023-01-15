package com.turtlpass.module.useraccount.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.turtlpass.module.useraccount.model.UserAccount
import com.turtlpass.module.useraccount.repo.AccountIdRepository
import com.turtlpass.rule.StandardCoroutineRule
import com.turtlpass.rule.runTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class RetrieveAccountUseCaseTest {

    @get:Rule var standardCoroutineRule = StandardCoroutineRule()

    private val repository = mockk<AccountIdRepository>(relaxed = true)

    @Test
    fun `Given a package name When retrieveAccount is called Then the correct UserAccount is returned`() {
        standardCoroutineRule.runTest { testDispatcher ->
            // Set up test data
            val packageName = "com.example.app"
            val email = "user@example.com"
            val userAccount = UserAccount(email, null)

            // Set up mock behaviors
            every { repository.retrieveAccountId(packageName) } returns email

            // Create the use case
            val retrieveAccountUseCase =
                RetrieveAccountUseCase(testDispatcher, repository)

            // Test the flow
            retrieveAccountUseCase(packageName).test {
                assertThat(awaitItem()).isEqualTo(userAccount)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Given no account id saved, When invoke is called with package name, Then null is returned`() {
        standardCoroutineRule.runTest { testDispatcher ->
            // Set up test data
            val packageName = "com.example.app"

            // Set up mock behaviors
            every { repository.retrieveAccountId(packageName) } returns null

            // Create the use case
            val retrieveAccountUseCase =
                RetrieveAccountUseCase(testDispatcher, repository)

            // Test the flow
            retrieveAccountUseCase(packageName).test {
                assertThat(awaitItem()).isNull()
                awaitComplete()
            }
        }
    }
}
