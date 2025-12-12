package com.turtlpass.usb.model

data class KeyDerivationInput(
    val topLevelDomain: String,
    val accountId: String,
    val pin: String,
)
