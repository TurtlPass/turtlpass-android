package com.turtlpass.appmanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.turtlpass.ui.theme.AppTheme.dimensions

@Composable
fun AndroidIconLayout(
    modifier: Modifier = Modifier,
    image: @Composable () -> Unit,
    label: @Composable () -> Unit
) {
    Column(
        modifier = modifier.padding(all = dimensions.x8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        image()
        label()
    }
}
