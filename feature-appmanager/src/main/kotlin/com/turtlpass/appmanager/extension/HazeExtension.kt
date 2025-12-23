package com.turtlpass.appmanager.extension

import androidx.compose.ui.Modifier
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

fun Modifier.conditionalHaze(
    enabled: Boolean,
    hazeState: HazeState
): Modifier {
    return if (enabled) {
        this.hazeSource(hazeState)
    } else {
        this
    }
}
