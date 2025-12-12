package com.turtlpass.appmanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.turtlpass.ui.theme.AppTheme.dimensions

@Composable
fun AndroidAdaptiveIconLayout(
    modifier: Modifier = Modifier,
    drawable: @Composable () -> Unit,
    label: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .requiredWidth(125.dp)
            .wrapContentSize()
            .padding(vertical = dimensions.x2),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        drawable()
        label()
    }
}
