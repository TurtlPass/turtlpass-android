plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.turtlpass.useraccount"
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

        buildConfigField("String", "GRAVATAR_HOST", "\"s.gravatar.com\"")
        buildConfigField("String", "GRAVATAR_PIN_SET", "\"sha256/xAiTj+krWlCqnOsDColRPKS9JppjzZ15bvE+oNlMswk=\"")
        buildConfigField("String", "AVATAR_LIARA_HOST", "\"avatar.iran.liara.run\"")
        buildConfigField("String", "AVATAR_LIARA_PIN_SET", "\"sha256/d1/Kxmk/mF0+kMdxusbSms/pXXUmz+vus2AuWZhUmiM=\"")
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
    implementation(project(":core-domain"))
    implementation(project(":core-network"))
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
    // Utilities
    implementation(libs.accompanist.permissions)
    implementation(libs.compose.placeholder.material)
    implementation(libs.lottie.compose)
    implementation(libs.timber)
}
