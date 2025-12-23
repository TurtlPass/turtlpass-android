import java.util.Properties
val localProps = Properties()
localProps.load(project.rootProject.file("local.properties").inputStream())

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.junit5)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.turtlpass"
    compileSdk {
        version = release(36)
    }
    buildToolsVersion = "36.0.0"

    defaultConfig {
        applicationId = "com.turtlpass"
        minSdk = 26
        targetSdk = 36
        versionCode = 30000
        versionName = "3.0.0"
        vectorDrawables { useSupportLibrary = true }
    }
    
    flavorDimensions += "environment"
    productFlavors {
        create("prod") {
            isDefault = true
            dimension = "environment"
        }
        create("mock") {
            dimension = "environment"
            versionNameSuffix = "-mock"
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(localProps.getProperty("turtlpassKeystore"))
            storePassword = localProps.getProperty("turtlpassKeystorePassword")
            keyAlias = localProps.getProperty("turtlpassKeystoreKeyAlias")
            keyPassword = localProps.getProperty("turtlpassKeystoreKeyPassword")
        }
    }

    buildTypes {
        release {
            getByName("release") {
                // Enables code shrinking, obfuscation, and optimization
                isMinifyEnabled = true
                // Enables resource shrinking, performed by the Android Gradle plugin
                isShrinkResources = true
                // Includes ProGuard rules files that are packaged with Android Gradle plugin
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
            signingConfig = signingConfigs.getByName("release")
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
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
    implementation(project(":core-ui"))
    implementation(project(":core-model"))
    implementation(project(":core-db"))
    implementation(project(":core-domain"))
    implementation(project(":feature-useraccount"))
    implementation(project(":feature-appmanager"))
    implementation(project(":feature-urlmanager"))
    implementation(project(":feature-usb"))
    implementation(project(":feature-biometric"))
    implementation(project(":feature-accessibility"))
    // Core
    implementation(libs.kotlin.stdlib)
    implementation(libs.core.ktx)
    implementation(libs.coroutines.android)
    implementation(libs.serialization)
    implementation(libs.splashscreen)
    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.animation.core)
    implementation(libs.androidx.lifecycle.process)
    debugImplementation(libs.compose.ui.tooling)
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
    // Protobuf
    implementation(libs.bundles.protobuf)
    // Network
    implementation(libs.bundles.network)
    // Coil
    implementation(libs.bundles.coil)
    // Haze
    implementation(libs.bundles.haze)
    // KDF
    implementation(libs.argon2)

    // Utilities
    implementation(libs.accompanist.permissions)
    implementation(libs.compose.placeholder.material)
    implementation(libs.lottie.compose)
    implementation(libs.timber)
    implementation(libs.usb.serial)
    implementation(libs.guava)
    debugImplementation(libs.leakcanary)

    //# Test dependencies
    testImplementation(libs.bundles.testUnit)
    androidTestImplementation(libs.bundles.testAndroid)
    // hilt
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.testing)
    // junit5
    testRuntimeOnly(libs.junit5.engine)
    testRuntimeOnly(libs.junit5.vintage)
}

room {
    schemaDirectory("$projectDir/schemas")
}
