@file:Suppress("unused")
package internal.dependencies

import internal.*

/**
 * Dagger - Hilt
 */
object Di {
    const val daggerHiltAndroid = "com.google.dagger:hilt-android:${Versions.hiltCore}"
    const val daggerHiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hiltCore}"
    const val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.hilt}"
    const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0"
    const val daggerHiltAndroidTesting = "com.google.dagger:hilt-android-testing:${Versions.hiltCore}"
}
