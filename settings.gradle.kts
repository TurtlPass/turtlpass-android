pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

// enables type-safe project accessors
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "turtlpass-android"

include(":app")
include(":core-ui")
include(":core-di")
include(":core-domain")
include(":core-model")
include(":core-db")
include(":core-network")
include(":feature-useraccount")
include(":feature-appmanager")
include(":feature-urlmanager")
include(":feature-accessibility")
include(":feature-biometric")
include(":feature-usb")
