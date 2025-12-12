package com.turtlpass.ui.swipeReveal

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue

class SwipeRevealState(
    val maxRevealPx: Float
) {
    var offsetPx by mutableFloatStateOf(0f)
        private set

    val isRevealed get() = offsetPx <= -maxRevealPx

    suspend fun reveal() {
        animateTo(-maxRevealPx)
    }

    suspend fun reset() {
        animateTo(0f)
    }

    private suspend fun animateTo(target: Float) {
        animate(
            initialValue = offsetPx,
            targetValue = target,
            animationSpec = tween(durationMillis = 200)
        ) { value, _ ->
            offsetPx = value
        }
    }

    fun dragBy(delta: Float) {
        val newValue = (offsetPx + delta).coerceIn(-maxRevealPx, 0f)
        offsetPx = newValue
    }
}
