package com.turtlpass.module.chooser.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.turtlpass.module.chooser.model.ChooserInputs
import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.module.useraccount.model.UserAccount
import com.turtlpass.module.useraccount.repo.AccountIdRepository
import com.turtlpass.rule.StandardCoroutineRule
import com.turtlpass.rule.runTest
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import org.junit.Rule
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class HashUserInputUseCaseTest {

    @get:Rule var standardCoroutineRule = StandardCoroutineRule()
    private val accountRepository = mockk<AccountIdRepository>(relaxed = true)

    @Test
    fun `Given a ChooserModel When hashUserInput is called Then the correct hash is returned`() {
        standardCoroutineRule.runTest { testDispatcher ->
            // Set up test data
            val topLevelDomain = "example"
            val email = "user@example.com"
            val pin = "123456"
            val packageName = "com.example.app"
            val installedApp = InstalledApp("Example App", packageName, topLevelDomain)
            val userAccount = UserAccount(email, null)
            val chooserInputs = ChooserInputs(installedApp = installedApp, userAccount = userAccount, pin = pin)

            // Create the use case
            val hashUserInputUseCase = HashUserInputUseCase(
                dispatcher = testDispatcher,
                accountRepository = accountRepository
            )

            // Test the flow
            hashUserInputUseCase(chooserInputs).test {
                advanceUntilIdle()

                verify { accountRepository.persistAccountId(packageName, email) }

                val expectedHash = sha512(pin + topLevelDomain + email)

                advanceTimeBy(2000L)
                runCurrent()

                assertThat(awaitItem()).isEqualTo(expectedHash)
                awaitComplete()
            }
        }
    }
}
