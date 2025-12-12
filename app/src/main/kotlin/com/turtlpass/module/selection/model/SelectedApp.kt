package com.turtlpass.module.selection.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedApp(
    val appName: String,
    val packageName: String,
    val topLevelDomain: String = "",
) : Parcelable
