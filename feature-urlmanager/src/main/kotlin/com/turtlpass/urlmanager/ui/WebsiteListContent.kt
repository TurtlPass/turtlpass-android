package com.turtlpass.urlmanager.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.turtlpass.model.WebsiteUi
import com.turtlpass.ui.theme.AppTheme.dimensions
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

@Composable
fun WebsiteListContent(
    websiteList: List<WebsiteUi>,
    hazeState: HazeState,
    expandedItemKey: String?,
    onExpandChanged: (String?) -> Unit,
    onSelectWebsite: (WebsiteUi) -> Unit,
    onDeleteWebsite: (WebsiteUi) -> Unit,
) {
    val listState = rememberLazyListState()
    val isScrolling by remember {
        derivedStateOf { listState.isScrollInProgress }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = dimensions.x16),
    ) {
        items(
            items = websiteList,
            key = { "${it.timestamp}_${it.url}" }
        ) { websiteUi ->
            val itemKey = "${websiteUi.timestamp}_${websiteUi.url}"

            SwipeRevealUrlItem(
                modifier = Modifier
                    .padding(vertical = dimensions.x4)
                    .hazeSource(hazeState)
                    .then(
                        if (!isScrolling) {
                            Modifier.animateItem(
                                fadeInSpec = tween(150),
                                fadeOutSpec = tween(250),
                                placementSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                            )
                        } else {
                            Modifier
                        }
                    ),
                websiteUi = websiteUi,
                isExpanded = expandedItemKey == itemKey,
                onExpandChanged = { expanded ->
                    onExpandChanged(if (expanded) itemKey else null)
                },
                onSelectWebsite = { onSelectWebsite(websiteUi) },
                onDelete = { onDeleteWebsite(websiteUi) }
            )
        }
    }
}
