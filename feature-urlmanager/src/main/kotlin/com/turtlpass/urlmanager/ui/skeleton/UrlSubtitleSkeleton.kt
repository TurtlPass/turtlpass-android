package com.turtlpass.urlmanager.ui.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.turtlpass.ui.anim.shimmerOverContent
import com.turtlpass.ui.theme.AppTheme.colors

@Composable
fun UrlSubtitleSkeleton() {
    Box(
        modifier = Modifier
            .height(12.dp)
            .fillMaxWidth(0.4f)
            .clip(RoundedCornerShape(4.dp))
            .background(colors.default.placeholder)
            .shimmerOverContent(
                highlightColor = colors.default.placeholderHighlight
            )
    )
}
