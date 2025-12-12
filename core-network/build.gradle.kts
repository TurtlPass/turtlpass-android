plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.junit5)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.turtlpass.network"
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

        buildConfigField("Long", "TIMEOUT_MILLIS", "5000L")
        resValue("bool", "uses_clear_text_traffic", "false")
        resValue("string", "gravatar_base_url", "gravatar.com")
        resValue("string", "avatar_base_url", "avatar.iran.liara.run")
        resValue("string", "google_base_url", "google.com")
        resValue("string", "gstatic_base_url", "gstatic.com")
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
    // Protobuf
    implementation(libs.bundles.protobuf)
    // Coil
    implementation(libs.bundles.coil)
    // Network
    implementation(libs.bundles.network)
    // Chucker
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.noop)
    // Utilities
    implementation(libs.accompanist.permissions)
    implementation(libs.compose.placeholder.material)
    implementation(libs.lottie.compose)
    implementation(libs.palette.ktx)
    implementation(libs.timber)
    implementation(libs.usb.serial)
    implementation(libs.guava)
}
