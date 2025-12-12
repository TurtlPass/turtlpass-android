package com.turtlpass.appmanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.turtlpass.ui.theme.AppTheme.dimensions

@Composable
fun AndroidIconLayout(
    modifier: Modifier = Modifier,
    image: @Composable () -> Unit,
    label: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(dimensions.cornerRadius))
            .padding(vertical = dimensions.x8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        image()
        label()
    }
}
