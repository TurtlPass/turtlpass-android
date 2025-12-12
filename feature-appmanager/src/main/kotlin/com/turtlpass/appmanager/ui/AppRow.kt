package com.turtlpass.appmanager.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.ui.theme.AppTheme.dimensions
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LazyItemScope.AppRow(
    row: List<InstalledAppUi>,
    hazeState: HazeState,
    onAppSelected: (InstalledAppUi) -> Unit,
    columns: Int
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(180)),
        exit = fadeOut(animationSpec = tween(120))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.x16, vertical = dimensions.x8)
                .animateItem(), // smooth reposition
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Add each icon
            row.forEach { app ->
                key(app.packageName) {
                    Box(
                        modifier = Modifier
                            .hazeSource(hazeState)
                            .clip(RoundedCornerShape(dimensions.x8))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(),
                                onClick = {
                                    //onClick = { if (lazyGridState?.isScrollInProgress == false) onClick() }
                                    onAppSelected(app)
                                }
                            )
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        AndroidIcon(
                            modifier = Modifier.fillMaxWidth(),
                            app = app,
                        )
                    }
                }
            }

            // Fill the rest of the row with invisible boxes (spacers)
            val emptySlots = columns - row.size
            repeat(emptySlots) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
