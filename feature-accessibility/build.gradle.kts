plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.turtlpass.accessibility"
    compileSdk {
        version = release(36)
    }
    buildToolsVersion = "36.0.0"

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Feature Modules
    implementation(project(":core-di"))
    implementation(project(":core-model"))
    implementation(project(":core-db"))
    // Core
    implementation(libs.kotlin.stdlib)
    implementation(libs.core.ktx)
    implementation(libs.coroutines.android)
    implementation(libs.serialization)
    // Lifecycle
    implementation(libs.bundles.lifecycle)
    // AndroidX
    implementation(libs.bundles.androidX)
    // Dependency Injection
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)
    // DB
    implementation(libs.bundles.db)
    ksp(libs.androidx.room.compiler)
    // Utilities
    implementation(libs.timber)
    implementation(libs.guava)
}

room {
    schemaDirectory("$projectDir/schemas")
}
