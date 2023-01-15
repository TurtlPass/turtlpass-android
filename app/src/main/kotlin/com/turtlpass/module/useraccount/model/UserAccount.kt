package com.turtlpass.module.useraccount.model

import android.net.Uri

data class UserAccount(
    val accountId: String,
    val thumbnailUri: Uri? = null,
    val gravatarUrl: String? = null,
)
