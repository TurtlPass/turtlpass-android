plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("internal")
    id("de.mannodermaus.android-junit5")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    compileSdk = internal.Android.compileSdk
    buildToolsVersion = internal.Android.buildTools

    defaultConfig {
        applicationId = "com.turtlpass"
        minSdk = internal.Android.minSdk
        targetSdk = internal.Android.targetSdk
        versionCode = 10100
        versionName = "1.1.0"
        vectorDrawables { useSupportLibrary = true }
        missingDimensionStrategy("device", "anyDevice")
        buildConfigField("Long", "TIMEOUT_MILLIS", "5000L")
        resValue("bool", "uses_clear_text_traffic", "false")
        resValue("string", "gravatar_base_url", "https://s.gravatar.com")
        buildConfigField("String", "GRAVATAR_BASE_URL", "\"https://s.gravatar.com\"")
        buildConfigField("String", "GRAVATAR_PIN_SET", "\"sha256/sSAE6ZWFQvZ1mQB8kh4utc/VpbMVSPQEuedwea9FrtM=\"")
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
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = internal.Versions.compose
    }
    packagingOptions {
        resources {
            excludes.add("META-INF/AL2.0")
            excludes.add("META-INF/LGPL2.1")
        }
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_11
        sourceCompatibility = JavaVersion.VERSION_11
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    internal.Dependencies.kotlin.apply {
        implementation(jetpackCore)
        implementation(splashscreen)
        implementation(kotlinStdlib)
        implementation(coroutinesAndroid)
        implementation(serialization)
    }
    internal.Dependencies.compose.apply {
        implementation(material)
        implementation(material3)
        implementation(ui)
        implementation(uiToolingPreview)
        debugImplementation(uiTooling)
        implementation(activityCompose)
        implementation(runtime)
        implementation(foundation)
        implementation(foundationLayout)
        implementation(constraintLayout)
        implementation(animation)
        implementation(navigationAnimation)
        implementation(systemUiController)
        implementation(permissions)
        implementation(placeholder)
        implementation(navigationMaterial)
        implementation(navigationCompose)
        implementation(coil)
        implementation(lottie)
    }
    internal.Dependencies.di.apply {
        implementation(daggerHiltAndroid)
        kapt(daggerHiltAndroidCompiler)
        implementation(hiltNavigationCompose)
        // test
        androidTestImplementation(daggerHiltAndroidTesting)
        kaptAndroidTest(daggerHiltAndroidCompiler)
    }
    internal.Dependencies.network.apply {
        implementation(okhttp)
        implementation(interceptor)
    }
    internal.Dependencies.security.apply {
        implementation(crypto)
    }
    internal.Dependencies.other.apply {
        implementation(appcompat)
        implementation(annotation)
        implementation(material)
        implementation(lifecycleViewModelKtx)
        implementation(lifecycleViewModelCompose)
        implementation(lifecycleViewModelSavedState)
        implementation(lifecycleRuntime)
        implementation(datastorePreferences)
        implementation(preferenceKtx)
        implementation(timber)
        implementation(usbSerial)
        implementation(guava)
        debugImplementation(leakcanary)
        debugImplementation(chucker)
        releaseImplementation(chuckerNoOp)
    }
    internal.Dependencies.test.apply {
        testImplementation(truth)
        testImplementation(turbine)
        testImplementation(coreTesting)
        testImplementation(coroutines)
        testImplementation(mockitoKotlin)
        testImplementation(mockk)
        testImplementation(mockkAgentJvm)
        // androidTestImplementation(mockkAndroid)
        // androidTestImplementation(mockkAgentJvm)
        testImplementation(junit5JupiterApi)
        testRuntimeOnly(junit5JupiterEngine)
        testImplementation(junit5JupiterParams)
        testRuntimeOnly(junit5VintageEngine)
        // androidTestImplementation(junit5Core)
        // androidTestRuntimeOnly(junit5Runner)
        testImplementation(sharedPreferencesMock)
    }
}
// Allow references to generated code (Hilt)
kapt {
    correctErrorTypes = true
}
