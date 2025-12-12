package com.turtlpass.appmanager.ui.skeleton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.turtlpass.ui.theme.AppTheme.dimensions

@Composable
fun AppRowSkeleton(
    columns: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.x16, vertical = dimensions.x8),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(columns) {
            AndroidIconSkeleton(
                modifier = Modifier.weight(1f)
            )
        }
    }
}
