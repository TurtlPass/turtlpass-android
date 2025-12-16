package com.turtlpass.appmanager.ui.skeleton

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.turtlpass.ui.theme.AppTheme.dimensions
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

@Composable
fun RecentAppsRowSkeleton(
    modifier: Modifier = Modifier,
    itemCount: Int = 6,
    hazeState: HazeState,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(itemCount) { index ->
            val itemModifier = when (index) {
                0 -> Modifier
                    .padding(start = dimensions.x8)
                itemCount - 1 -> Modifier
                    .padding(end = dimensions.x8)
                else -> Modifier
            }

            AndroidAdaptiveIconSkeleton(
                modifier = itemModifier.hazeSource(hazeState)
            )
        }
    }
}
