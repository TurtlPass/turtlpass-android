package com.turtlpass.urlmanager.extension

fun String.faviconUrl(size: Int = 64): String =
    "https://www.google.com/s2/favicons?sz=$size&domain_url=$this"
