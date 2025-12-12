package com.turtlpass.urlmanager.extension

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun DateTimeFormatter.formatTime(timestamp: Long): String = LocalDateTime.ofInstant(
    /* instant = */ Instant.ofEpochMilli(timestamp),
    /* zone = */ ZoneId.systemDefault()
).format(this)
