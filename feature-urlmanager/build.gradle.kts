plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.turtlpass.urlmanager"
    compileSdk {
        version = release(36)
    }
    buildToolsVersion = "36.0.0"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "GOOGLE_HOST", "\"www.google.com\"")
        buildConfigField("String", "GOOGLE_SPKI_PIN", "\"sha256/iXK48jI+5eMBOyAex+NUO125Sv+Wx0hfOyQHdyuQ2pQ=\"")
        buildConfigField("String", "GSTATIC_HOST", "\".gstatic.com\"")
        buildConfigField("String", "GSTATIC_SPKI_PIN", "\"sha256/NeARMRZSkfI40s+ApV0QxHMvkN5y8VQ+kCiwH9Hl/e0=\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Feature Modules
    implementation(project(":core-di"))
    implementation(project(":core-ui"))
    implementation(project(":core-model"))
    implementation(project(":core-domain"))
    implementation(project(":core-db"))
    implementation(project(":core-network"))
    implementation(project(":feature-accessibility"))
    implementation(project(":feature-appmanager"))
    // Core
    implementation(libs.kotlin.stdlib)
    implementation(libs.core.ktx)
    implementation(libs.coroutines.android)
    implementation(libs.serialization)
    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.compose.ui.tooling)
    // Lifecycle
    implementation(libs.bundles.lifecycle)
    // AndroidX
    implementation(libs.bundles.androidX)
    // Dependency Injection
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)
    // Network
    implementation(libs.bundles.network)
    // Coil
    implementation(libs.bundles.coil)
    // Haze
    implementation(libs.bundles.haze)
    // Utilities
    implementation(libs.accompanist.permissions)
    implementation(libs.compose.placeholder.material)
    implementation(libs.lottie.compose)
    implementation(libs.timber)
    implementation(libs.guava)
}
