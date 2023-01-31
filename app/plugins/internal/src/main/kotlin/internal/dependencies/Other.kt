@file:Suppress("unused")
package internal.dependencies

import internal.*

object Other {
    const val appcompat = "androidx.appcompat:appcompat:1.4.1"
    const val annotation = "androidx.annotation:annotation:1.5.0"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleViewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle}"
    const val lifecycleViewModelSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycleRuntimeCompose = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycle}"
    const val datastorePreferences = "androidx.datastore:datastore-preferences:1.1.0-alpha01"
    const val preferenceKtx = "androidx.preference:preference-ktx:1.2.0"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val usbSerial = "com.github.felHR85:UsbSerial:6.1.0"
    const val leakcanary = "com.squareup.leakcanary:leakcanary-android:2.7"
    const val guava = "com.google.guava:guava:31.1-android"
    const val chucker = "com.github.chuckerteam.chucker:library:3.5.2"
    const val chuckerNoOp = "com.github.chuckerteam.chucker:library-no-op:3.5.2"
}
