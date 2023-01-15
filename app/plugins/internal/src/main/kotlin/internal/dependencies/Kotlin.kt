@file:Suppress("unused")

package internal.dependencies

import internal.Versions

object Kotlin {
    const val jetpackCore = "androidx.core:core-ktx:${Versions.jetpackCore}"
    const val splashscreen = "androidx.core:core-splashscreen:1.0.0-beta02"
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.ktxCore}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.ktxCoroutinesAndroid}"
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}"
}
