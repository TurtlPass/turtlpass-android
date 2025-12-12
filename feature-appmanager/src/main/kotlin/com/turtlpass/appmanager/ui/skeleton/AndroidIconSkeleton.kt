package com.turtlpass.appmanager.ui.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.turtlpass.appmanager.ui.AndroidIconLayout
import com.turtlpass.ui.anim.shimmerOverContent
import com.turtlpass.ui.icons.Turtlpass24Px
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.Grey200

@Composable
fun AndroidIconSkeleton(
    modifier: Modifier = Modifier,
) {
    AndroidIconLayout(
        modifier = modifier,
        image = {
            // Circle image skeleton
            Box(
                modifier = Modifier
                    .padding(horizontal = dimensions.x4 + dimensions.x2)
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(colors.default.placeholder)
                    .shimmerOverContent(
                        highlightColor = colors.default.placeholderHighlight
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Turtlpass24Px,
                    contentDescription = null,
                    tint = Grey200,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        },
        label = {
            // Text skeleton
            Spacer(
                modifier = Modifier
                    .padding(top = dimensions.x4)
                    .height(12.dp) // matches text size
                    .fillMaxWidth(0.7f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.default.placeholder)
                    .shimmerOverContent(
                        highlightColor = colors.default.placeholderHighlight
                    )
            )
        }
    )
}
