@file:Suppress("unused")
package internal.dependencies

import internal.*

object Security {
    /**
     * [Crypto](https://developer.android.com/jetpack/androidx/releases/security)
     */
    const val crypto = "androidx.security:security-crypto:${Versions.securityCrypto}"
    const val biometric = "androidx.biometric:biometric:1.2.0-alpha05"
    const val argon2 = "com.lambdapioneer.argon2kt:argon2kt:1.3.0"
}
