package com.turtlpass.module.installedapp.ui

import com.turtlpass.common.compose.column.StickyChar
import com.turtlpass.module.installedapp.model.InstalledApp

class InstalledAppWrapper(
    val installedApp: InstalledApp
) : StickyChar {

    override val char: Char
        get() = installedApp.appName.first().uppercaseChar()
}
