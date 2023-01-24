package com.turtlpass.module.chooser.usecase

//FIXME: Write instrumentation test as Argon2Kt cannot be unit tested
/*@ExperimentalCoroutinesApi
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
}*/
