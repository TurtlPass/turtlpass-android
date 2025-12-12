<p align="center">
  <img src="https://raw.githubusercontent.com/TurtlPass/turtlpass-firmware-arduino/master/assets/icon.png" alt="Logo" width="133"/>
</p>

<h2 align="center">🔗 TurtlPass Ecosystem</h2>

<p align="center">
  🐢 <a href="https://github.com/TurtlPass/turtlpass-firmware-arduino"><b>Firmware</b></a> •
  💾 <a href="https://github.com/TurtlPass/turtlpass-protobuf"><b>Protobuf</b></a> •
  💻 <a href="https://github.com/TurtlPass/turtlpass-python"><b>Host</b></a> •
  🌐 <a href="https://github.com/TurtlPass/turtlpass-chrome-extension"><b>Chrome</b></a> •
  📱 <a href="https://github.com/TurtlPass/turtlpass-android"><b>Android</b></a>
</p>

---

# 📱 TurtlPass Android

[![](https://img.shields.io/github/v/release/TurtlPass/turtlpass-android?color=green&label=Release&logo=android)](https://github.com/TurtlPass/turtlpass-android/releases/latest "GitHub Release")
![Kotlin](https://img.shields.io/badge/Kotlin-2.2+-blue?logo=kotlin)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-blue?logo=jetpackcompose)
![USB/HID](https://img.shields.io/badge/USB-Serial%20%2F%20HID-blue?logo=usb)
[![](https://img.shields.io/badge/protobuf-v3-blue)](https://developers.google.com/protocol-buffers "Protocol Buffers")
[![Argon2](https://img.shields.io/badge/KDF-Argon2-informational)](#)
[![](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT "License: MIT")

A secure, hardware-backed password generator that connects your Android phone to the TurtlPass USB device via Serial & Protobuf.
Passwords are generated on-device, and when you press the physical button, the device emulates a keyboard and types your password — safely and locally, without ever exposing it online.

---

## ⚡ Features

* 🔒 Hardware-based password generation
* 📱 Discoverable list of installed **Android apps** with search
* 🌐 Optional **Websites** auto-detection
* 💾 Stores and recalls **Account IDs** locally
* 🧩 Protobuf-based communication with the TurtlPass device
* ⌨️ One-button password typing — no clipboard, no exposure

---

## 🔐 Cryptographic Workflow (App ↔ Device)

The Android app prepares all cryptographic inputs locally, while the TurtlPass MCU validates the request, runs the KDF, and types the password securely via USB HID.  
The Android app never **sees or stores your generated password**.

```
+--------------------------+     Protobuf over USB     +------------------+
|       Android App        |  <--------------------->  | TurtlPass Device |
|--------------------------|                           |------------------|
| Hash(Argon2ID + SHA-512) |    Serialized Commands:   |     Generate     |
|            of            |   → genPassword(hash) →   |     Password     |
| Domain + AccountID + PIN |    ← isSuccess(bool) ←    |      (KDF)       |
+--------------------------+                           +------------------+
             |                                                   |
             └──────────< Types password via HID keyboard <──────┘
```

---

## 🚀 Usage

1. Choose the **App** or **Website** you want a password for
2. Type in your `Account ID`, typically your email address
3. Click **Get Password** and enter your 6-digit PIN
4. Plug the TurtlPass device into your phone's USB port
5. The TurtlPass hardware performs the KDF to generate your password
6. Press the **device button** — it types your password securely in the focused input field

---

## 📦 Module Architecture

TurtlPass Android uses a scalable multi-module setup aligned with Clean Architecture.
Each module has a clearly defined responsibility to ensure testability, faster builds, and strict separation of concerns.

### **Core Modules**

* **core-ui**: Shared Compose components
* **core-di**: Global Hilt bindings
* **core-domain**: Shared Kotlin logic
* **core-model**: Shared Kotlin data models
* **core-db**: Local persistence layer
* **core-network**: Networking layer

### **Feature Modules**

* **feature-useraccount**: Account ID management and related UI.
* **feature-appmanager**: Installed app listing, filtering, and lookup.
* **feature-urlmanager**: Website handling and related UI.
* **feature-accessibility**: Accessibility for App/URL detection.
* **feature-biometric**: Biometric authentication UI + logic.
* **feature-usb**: USB Serial + Protobuf communication with the TurtlPass device.

This structure combines vertical features with horizontal core layers, keeping the codebase clean, maintainable, and easy to extend.

---

## 🏛️ Clean Architecture

* [Kotlin](https://kotlinlang.org/), [Coroutines](https://github.com/Kotlin/kotlinx.coroutines), [Flow / StateFlow](https://kotlinlang.org/docs/flow.html)
* [MVVM](https://developer.android.com/topic/libraries/architecture/viewmodel), [Use Cases](https://developer.android.com/topic/architecture/domain-layer#use-cases-kotlin), [Repositories](https://developer.android.com/topic/architecture#data-layer)
* [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)
* [Jetpack Compose UI](https://developer.android.com/jetpack/androidx/releases/compose-ui)
* [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)

---

## 📚 Dependencies

**Third-party libraries used in the project:**

[Hilt](https://dagger.dev/hilt/), [Coil](https://github.com/coil-kt/coil), [OkHttp](https://github.com/square/okhttp), [UsbSerial](https://github.com/felHR85/UsbSerial), [Argon2](https://github.com/lambdapioneer/argon2kt), [Lottie](https://github.com/airbnb/lottie-android), etc.

**Libraries used in the Unit Tests:**

[JUnit](https://junit.org/junit5/), [Mockk](https://github.com/mockk/mockk),  [Truth](https://github.com/google/truth) & [Turbine](https://github.com/cashapp/turbine)

---

## 📜 License

This repository is licensed under the [MIT License](./LICENSE).
