package com.turtlpass.appmanager.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.turtlpass.appmanager.R
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

@Composable
fun RecentAppsHeader(isSearchFocused: Boolean, hazeState: HazeState) {
    AnimatedVisibility(visible = !isSearchFocused) {
        Row(
            modifier = Modifier
                .hazeSource(hazeState)
                .fillMaxWidth()
                .padding(top = dimensions.x16)
                .padding(bottom = dimensions.x8)
                .padding(horizontal = dimensions.x16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.feature_appmanager_recently_used),
                style = typography.h2.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                ),
            )
        }
    }
}
