package com.turtlpass.usb.extension

import androidx.annotation.Keep
import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2Mode
import com.lambdapioneer.argon2kt.Argon2Version

@Keep
fun Argon2Kt.kdf(pin: String, salt: String): String = hash(
    mode = Argon2Mode.ARGON2_ID,
    password = pin.toByteArray(),
    salt = salt.toByteArray(),
    tCostInIterations = 32, // number of iterations
    parallelism = 4, // number of threads in parallel
    mCostInKibibyte = 65536, // 64 MiB memory cost
    hashLengthInBytes = 64,
    version = Argon2Version.V13
).rawHashAsHexadecimal()
