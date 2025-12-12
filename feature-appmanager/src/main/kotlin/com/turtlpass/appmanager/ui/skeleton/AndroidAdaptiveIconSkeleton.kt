package com.turtlpass.appmanager.ui.skeleton

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.turtlpass.appmanager.ui.AndroidAdaptiveIconLayout

@Composable
fun AndroidAdaptiveIconSkeleton(
    modifier: Modifier = Modifier,
) {
    AndroidAdaptiveIconLayout(
        modifier = modifier,
        drawable = {
            InstalledAppAdaptiveDrawableSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(150.dp)
            )
        },
        label = {
            AdaptiveIconLabelSkeleton()
        }
    )
}
