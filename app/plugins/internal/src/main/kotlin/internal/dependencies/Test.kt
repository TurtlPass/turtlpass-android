@file:Suppress("unused")

package internal.dependencies

import internal.Versions

object Test {
    const val truth = "com.google.truth:truth:1.1.3"
    const val turbine = "app.cash.turbine:turbine:0.12.1"
    const val coreTesting = "androidx.arch.core:core-testing:2.1.0"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4"
    const val mockitoCore = "org.mockito:mockito-core:${Versions.mockitoCore}"
    const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:4.1.0"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val mockkAgentJvm = "io.mockk:mockk-agent-jvm:${Versions.compose}"
    const val mockkAndroid = "io.mockk:mockk-android:${Versions.compose}"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.mockWebServer}"
    const val junit5JupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junit5}"
    const val junit5JupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit5}"
    const val junit5JupiterParams = "org.junit.jupiter:junit-jupiter-params:${Versions.junit5}"
    const val junit5VintageEngine = "org.junit.vintage:junit-vintage-engine:${Versions.junit5}"
    const val junit5Core = "de.mannodermaus.junit5:android-test-core:1.3.0"
    const val junit5Runner = "de.mannodermaus.junit5:android-test-runner:1.3.0"
    const val sharedPreferencesMock = "io.github.ivanshafran:shared-preferences-mock:1.2.4"
}
