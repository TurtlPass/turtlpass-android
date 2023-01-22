package com.turtlpass.module.chooser.usecase

import com.turtlpass.di.IoDispatcher
import com.turtlpass.module.chooser.model.ChooserInputs
import com.turtlpass.module.useraccount.repo.AccountIdRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.security.MessageDigest
import javax.inject.Inject

class HashUserInputUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val accountRepository: AccountIdRepository,
) {
    operator fun invoke(chooserInputs: ChooserInputs): Flow<String> {
        return flow {
            val packageName = chooserInputs.installedApp?.packageName ?: ""
            val pin = chooserInputs.pin ?: ""
            val topLevelDomain = chooserInputs.installedApp?.topLevelDomain ?: ""
            val accountId = chooserInputs.userAccount?.accountId ?: ""
            accountRepository.persistAccountId(packageName, accountId)
            val hash = sha512(pin + topLevelDomain + accountId)
            delay(2000L)
            emit(hash)
        }.catch {
            emit("")
        }.flowOn(dispatcher)
    }
}

fun sha512(string: String): String {
    return MessageDigest.getInstance("SHA-512")
        .digest(string.toByteArray())
        .joinToString(separator = "") {
            ((it.toInt() and 0xff) + 0x100)
                .toString(16)
                .substring(1)
        }
}
