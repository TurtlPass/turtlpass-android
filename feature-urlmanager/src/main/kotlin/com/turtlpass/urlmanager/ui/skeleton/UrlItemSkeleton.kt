package com.turtlpass.urlmanager.ui.skeleton

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.turtlpass.urlmanager.ui.UrlItemLayout

@Composable
fun UrlItemSkeleton(
    modifier: Modifier = Modifier,
) {
    UrlItemLayout(
        modifier = modifier,
        onClick = null, // no interaction
        leading = { UrlLeadingIconsSkeleton() },
        titleRow = { UrlTitleRowSkeleton() },
        subtitle = { UrlSubtitleSkeleton() }
    )
}
