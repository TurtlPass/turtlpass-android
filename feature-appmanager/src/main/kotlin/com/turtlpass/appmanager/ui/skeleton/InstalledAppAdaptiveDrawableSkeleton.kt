package com.turtlpass.appmanager.ui.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.turtlpass.ui.anim.shimmerOverContent
import com.turtlpass.ui.icons.Turtlpass24Px
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.Grey200

@Composable
fun InstalledAppAdaptiveDrawableSkeleton(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(top = dimensions.x8)
            .padding(horizontal = dimensions.x8)
            .clip(RoundedCornerShape(dimensions.cornerRadius))
            .border(
                width = 1.dp,
                color = Grey200,
                shape = RoundedCornerShape(dimensions.cornerRadius),
            )
            .background(colors.default.placeholder)
            .shimmerOverContent(
                highlightColor = colors.default.placeholderHighlight
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(64.dp),
            imageVector = Turtlpass24Px,
            contentDescription = null,
            tint = Grey200,
        )
    }
}
