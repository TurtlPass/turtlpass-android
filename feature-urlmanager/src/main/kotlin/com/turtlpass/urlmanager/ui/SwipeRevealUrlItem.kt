package com.turtlpass.urlmanager.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.turtlpass.model.WebsiteUi
import com.turtlpass.ui.icons.DeleteForever24Px
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeRevealUrlItem(
    modifier: Modifier = Modifier,
    websiteUi: WebsiteUi,
    isExpanded: Boolean,
    onExpandChanged: (Boolean) -> Unit,
    onSelectWebsite: () -> Unit,
    onDelete: (WebsiteUi) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current

    val maxReveal = 64.dp
    val maxRevealPx = with(LocalDensity.current) { maxReveal.toPx() }

    // Animatable for X offset
    val offsetX = remember { Animatable(0f) }

    // Animate offset when parent-driven expansion changes
    LaunchedEffect(isExpanded) {
        val target = if (isExpanded) -maxRevealPx else 0f
        offsetX.animateTo(
            target,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
        // Play haptic once when expanded
        if (isExpanded) {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        BackgroundActionDelete(
            scope = scope,
            onDelete = onDelete,
            websiteUi = websiteUi,
            onExpandChanged = onExpandChanged,
            maxReveal = maxReveal
        )

        ForegroundContent(
            offsetX = offsetX,
            isExpanded = isExpanded,
            onSelectWebsite = onSelectWebsite,
            onExpandChanged = onExpandChanged,
            scope = scope,
            maxRevealPx = maxRevealPx,
            websiteUi = websiteUi
        ) {
            UrlItemContent(
                websiteUi = websiteUi,
                onClick = {
                    if (isExpanded) onExpandChanged(false)
                    onSelectWebsite()
                }
            )
        }
    }
}

@Composable
private fun ForegroundContent(
    offsetX: Animatable<Float, AnimationVector1D>,
    isExpanded: Boolean,
    onSelectWebsite: () -> Unit,
    onExpandChanged: (Boolean) -> Unit,
    scope: CoroutineScope,
    maxRevealPx: Float,
    websiteUi: WebsiteUi,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                colors.default.input,
                RoundedCornerShape(dimensions.cornerRadius)
            )
            .clip(RoundedCornerShape(dimensions.cornerRadius))
            .combinedClickable(
                onClick = {
                    if (!isExpanded) onSelectWebsite()
                    else onExpandChanged(false)
                }
            )
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    scope.launch {
                        val newOffset =
                            (offsetX.value + delta).coerceIn(-maxRevealPx, 0f)
                        offsetX.snapTo(newOffset)
                    }
                },
                onDragStopped = {
                    scope.launch {
                        val shouldExpand = offsetX.value <= -maxRevealPx / 2f
                        onExpandChanged(shouldExpand)

                        val target = if (shouldExpand) -maxRevealPx else 0f
                        offsetX.animateTo(
                            target,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                        )
                    }
                }
            )
    ) {
        content()
    }
}

@Composable
private fun BoxScope.BackgroundActionDelete(
    scope: CoroutineScope,
    onDelete: (WebsiteUi) -> Unit,
    websiteUi: WebsiteUi,
    onExpandChanged: (Boolean) -> Unit,
    maxReveal: Dp
) {
    Row(
        modifier = Modifier
            .matchParentSize()
            .padding(end = 16.dp + 1.dp), // 1.dp extra to hide bg completely
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                scope.launch {
                    onDelete(websiteUi)
                    onExpandChanged(false)
                }
            },
            modifier = Modifier
                .width(maxReveal + 16.dp)
                .fillMaxHeight()
                .background(
                    color = Color.Red,
                    shape = RoundedCornerShape(
                        topEnd = dimensions.cornerRadius,
                        bottomEnd = dimensions.cornerRadius
                    )
                )
        ) {
            Icon(
                modifier = Modifier.padding(start = 12.dp),
                imageVector = DeleteForever24Px,
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}
