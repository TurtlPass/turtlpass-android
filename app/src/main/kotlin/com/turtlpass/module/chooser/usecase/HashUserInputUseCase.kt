package com.turtlpass.module.chooser.usecase

import com.lambdapioneer.argon2kt.Argon2Kt
import com.turtlpass.common.extension.kdf
import com.turtlpass.common.extension.sha512
import com.turtlpass.di.IoDispatcher
import com.turtlpass.module.chooser.model.ChooserInputs
import com.turtlpass.module.useraccount.repo.AccountIdRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class HashUserInputUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val accountRepository: AccountIdRepository,
) {
    operator fun invoke(chooserInputs: ChooserInputs): Flow<String> {
        return flow {
            val packageName = requireNotNull(chooserInputs.installedApp?.packageName)
            val accountId = requireNotNull(chooserInputs.userAccount?.accountId)
            val topLevelDomain = requireNotNull(chooserInputs.installedApp?.topLevelDomain)
            accountRepository.persistAccountId(packageName, accountId)
            var password = requireNotNull(chooserInputs.pin)
            chooserInputs.passphrase?.let { passphrase -> password += passphrase }
            val salt = sha512(topLevelDomain + accountId)
            val argon2duration = measureTimeMillis {
                val hash = Argon2Kt().kdf(
                    password = password,
                    salt = salt
                )
                emit(hash)
            }
            Timber.i("Argon2 took $argon2duration milliseconds")
        }.catch {
            emit("")
        }.flowOn(dispatcher)
    }
}
