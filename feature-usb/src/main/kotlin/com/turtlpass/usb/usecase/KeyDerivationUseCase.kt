package com.turtlpass.usb.usecase

import com.lambdapioneer.argon2kt.Argon2Kt
import com.turtlpass.di.IoDispatcher
import com.turtlpass.domain.sha512
import com.turtlpass.usb.extension.kdf
import com.turtlpass.usb.model.KeyDerivationInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class KeyDerivationUseCase @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(input: KeyDerivationInput): Flow<String> {
        return flow {
            val salt = sha512(input.topLevelDomain + input.accountId)
            val argon2duration = measureTimeMillis {
                val hash = Argon2Kt().kdf(
                    pin = input.pin,
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
