package com.turtlpass.common.extension

import androidx.annotation.Keep
import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2Mode
import com.lambdapioneer.argon2kt.Argon2Version
import java.security.MessageDigest

@Keep
fun Argon2Kt.kdf(password: String, salt: String): String = hash(
    mode = Argon2Mode.ARGON2_I, // optimized for password hashing
    password = password.toByteArray(),
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
