package com.turtlpass.ui.swipeReveal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun rememberSwipeRevealState(maxRevealDp: Dp): SwipeRevealState {
    val px = with(LocalDensity.current) { maxRevealDp.toPx() }
    return remember { SwipeRevealState(px) }
}
