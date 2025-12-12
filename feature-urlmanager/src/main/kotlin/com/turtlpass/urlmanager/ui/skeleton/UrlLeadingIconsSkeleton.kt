package com.turtlpass.urlmanager.ui.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.turtlpass.ui.anim.shimmerOverContent
import com.turtlpass.ui.theme.AppTheme.colors

@Composable
fun UrlLeadingIconsSkeleton() {
    Box(modifier = Modifier.size(42.dp)) {

        Box(
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .background(colors.default.placeholder)
                .shimmerOverContent(
                    highlightColor = colors.default.placeholderHighlight
                )
        )

        Box(
            modifier = Modifier
                .requiredSize(16.dp)
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .background(colors.default.placeholder)
                .shimmerOverContent(
                    highlightColor = colors.default.placeholderHighlight
                )
        )
    }
}
