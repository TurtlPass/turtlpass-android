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
    namespace = "com.turtlpass.usb"
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
    implementation(project(":feature-accessibility"))
    implementation(project(":feature-appmanager"))
    // Core
    implementation(libs.kotlin.stdlib)
    implementation(libs.core.ktx)
    implementation(libs.coroutines.android)
    implementation(libs.serialization)
    implementation(libs.splashscreen)
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
    // KDF
    implementation(libs.argon2)
    // Utilities
    implementation(libs.accompanist.permissions)
    implementation(libs.compose.placeholder.material)
    implementation(libs.lottie.compose)
    implementation(libs.timber)
    implementation(libs.usb.serial)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.33.0"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite") // For smaller Android-compatible classes
                }
            }
        }
    }
}
