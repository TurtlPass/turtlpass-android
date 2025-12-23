package com.turtlpass.ui.anim

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset

@Composable
fun LazyGridItemScope.conditionalAnimateGridItem(
    isScrolling: Boolean,
    fadeInSpec: FiniteAnimationSpec<Float> = tween(120),
    fadeOutSpec: FiniteAnimationSpec<Float> = tween(90),
    placementSpec: FiniteAnimationSpec<IntOffset> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )
): Modifier {
    return if (!isScrolling) {
        Modifier.animateItem(
            fadeInSpec = fadeInSpec,
            fadeOutSpec = fadeOutSpec,
            placementSpec = placementSpec
        )
    } else {
        Modifier
    }
}
