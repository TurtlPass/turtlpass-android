package com.turtlpass.domain

import java.security.MessageDigest

fun sha512(string: String): String = MessageDigest.getInstance("SHA-512")
    .digest(string.toByteArray())
    .joinToString(separator = "") {
        ((it.toInt() and 0xff) + 0x100)
            .toString(16)
            .substring(1)
    }
