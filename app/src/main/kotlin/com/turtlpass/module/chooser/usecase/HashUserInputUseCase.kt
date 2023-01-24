package com.turtlpass.module.chooser.usecase

import androidx.annotation.Keep
import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2Mode
import com.lambdapioneer.argon2kt.Argon2Version
import com.turtlpass.di.IoDispatcher
import com.turtlpass.module.chooser.model.ChooserInputs
import com.turtlpass.module.useraccount.repo.AccountIdRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import java.security.MessageDigest
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class HashUserInputUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val accountRepository: AccountIdRepository,
) {
    operator fun invoke(chooserInputs: ChooserInputs): Flow<String> {
        return flow {
            val packageName = chooserInputs.installedApp?.packageName ?: ""
            val accountId = chooserInputs.userAccount?.accountId ?: ""
            val pin = chooserInputs.pin ?: ""
            val topLevelDomain = chooserInputs.installedApp?.topLevelDomain ?: ""
            val salt = sha512(topLevelDomain + accountId)
            accountRepository.persistAccountId(packageName, accountId)
            val argon2duration = measureTimeMillis {
                val hash = Argon2Kt().kdf(
                    pin = pin,
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

@Keep
fun Argon2Kt.kdf(pin: String, salt: String): String = hash(
    mode = Argon2Mode.ARGON2_I, // optimized for password hashing
    password = pin.toByteArray(),
    salt = salt.toByteArray(),
    tCostInIterations = 32, // number of iterations
    parallelism = 4, // number of threads in parallel
    mCostInKibibyte = 65536, // 64 MiB memory cost
    hashLengthInBytes = 64,
    version = Argon2Version.V13
).rawHashAsHexadecimal()

fun sha512(string: String): String = MessageDigest.getInstance("SHA-512")
    .digest(string.toByteArray())
    .joinToString(separator = "") {
        ((it.toInt() and 0xff) + 0x100)
            .toString(16)
            .substring(1)
    }
