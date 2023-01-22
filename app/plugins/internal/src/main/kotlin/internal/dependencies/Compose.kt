@file:Suppress("unused")

package internal.dependencies

import internal.Versions

object Compose {
    const val material3 = "androidx.compose.material3:material3:1.1.0-alpha03"
    const val material = "androidx.compose.material:material:${Versions.jetpack}"
    const val materialIcons = "androidx.compose.material:material-icons-extended:${Versions.jetpack}"
    const val ui = "androidx.compose.ui:ui:${Versions.jetpack}"
    const val foundation = "androidx.compose.foundation:foundation:${Versions.jetpack}"
    const val foundationLayout = "androidx.compose.foundation:foundation-layout:${Versions.jetpack}"
    const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.jetpack}"
    const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.jetpack}"
    const val runtime = "androidx.compose.runtime:runtime:${Versions.jetpack}"
    const val animation = "androidx.compose.animation:animation:${Versions.jetpack}"
    const val activityCompose = "androidx.activity:activity-compose:1.6.1"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.1"
    const val coil = "io.coil-kt:coil-compose:2.2.2"
    const val lottie = "com.airbnb.android:lottie-compose:5.2.0"
    const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}"
    const val navigationAnimation = "com.google.accompanist:accompanist-navigation-animation:${Versions.accompanist}"
    const val permissions = "com.google.accompanist:accompanist-permissions:${Versions.accompanist}"
    const val placeholder = "com.google.accompanist:accompanist-placeholder:${Versions.accompanist}"
    const val navigationMaterial = "com.google.accompanist:accompanist-navigation-material:${Versions.accompanist}"
    const val navigationCompose = "androidx.navigation:navigation-compose:2.5.3"
}
