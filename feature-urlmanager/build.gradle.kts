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
        buildConfigField("String", "GOOGLE_SPKI_PIN", "\"sha256/JCAFDAmMdfOmLIisB8eTtLgO2la69Ed7AzzZDyngI9g=\"")
        buildConfigField("String", "GSTATIC_HOST", "\".gstatic.com\"")
        buildConfigField("String", "GSTATIC_SPKI_PIN", "\"sha256/sDIcuByt0WP0MQ0UV0QZ71i4IQMaIQPXs1cRWh9d5YU=\"")
    }

    flavorDimensions += "environment"
    productFlavors {
        create("prod") {
            isDefault = true
            dimension = "environment"
        }
        create("mock") {
            dimension = "environment"
        }
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
