package com.turtlpass.ui.fling

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun rememberCustomFlingBehavior(
    velocityMultiplier: Float = 0.9f
): FlingBehavior {
    val decay = rememberSplineBasedDecay<Float>()

    return remember(decay, velocityMultiplier) {
        object : FlingBehavior {
            override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
                val velocity = initialVelocity * velocityMultiplier
                if (kotlin.math.abs(velocity) <= 1f) return velocity

                var velocityLeft = velocity
                var lastValue = 0f
                val animationState = AnimationState(initialValue = 0f, initialVelocity = velocity)

                try {
                    animationState.animateDecay(decay) {
                        val delta = value - lastValue
                        val consumed = scrollBy(delta)
                        lastValue = value
                        velocityLeft = this.velocity

                        if (kotlin.math.abs(delta - consumed) > 0.5f) cancelAnimation()
                    }
                } catch (_: CancellationException) {
                    velocityLeft = animationState.velocity
                }

                return velocityLeft
            }
        }
    }
}
