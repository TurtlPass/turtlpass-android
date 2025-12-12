package com.turtlpass.appmanager.ui.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.turtlpass.ui.anim.shimmerOverContent
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions

@Composable
fun AdaptiveIconLabelSkeleton() {
    Spacer(
        modifier = Modifier
            .padding(top = dimensions.x8)
            .padding(bottom = dimensions.x2)
            .height(14.dp)
            .fillMaxWidth(0.7f)
            .clip(RoundedCornerShape(4.dp))
            .background(colors.default.placeholder)
            .shimmerOverContent(
                highlightColor = colors.default.placeholderHighlight
            )
    )
}
