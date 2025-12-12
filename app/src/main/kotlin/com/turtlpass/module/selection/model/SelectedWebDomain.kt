package com.turtlpass.module.selection.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedWebDomain(
    val url: String = "",
    val topLevelDomain: String = "",
    val packageName: String = "",
    val faviconUrl: String = ""
) : Parcelable
