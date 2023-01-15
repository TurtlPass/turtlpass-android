package com.turtlpass.module.pin

import androidx.compose.ui.graphics.vector.ImageVector

sealed class PinKeyType {
    data class Text(val text: String) : PinKeyType()
    data class Vector(val imageVector: ImageVector) : PinKeyType()
    object None : PinKeyType()
}
