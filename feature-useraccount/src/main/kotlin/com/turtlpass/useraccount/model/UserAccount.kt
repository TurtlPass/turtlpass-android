package com.turtlpass.useraccount.model

import android.net.Uri

data class UserAccount(
    val accountId: String,
    val thumbnailUri: Uri? = null,
    val avatarUrl: String? = null,
)
