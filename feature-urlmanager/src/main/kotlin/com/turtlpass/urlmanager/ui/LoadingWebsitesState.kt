package com.turtlpass.urlmanager.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.urlmanager.ui.skeleton.UrlItemSkeleton

@Composable
fun LoadingWebsitesState(
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(12, key = { "skeleton_$it" }) {
            UrlItemSkeleton(
                modifier = Modifier
                    .padding(horizontal = dimensions.x16, vertical = dimensions.x4)
            )
        }
    }
}
