package com.turtlpass.urlmanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions

@Composable
fun UrlItemLayout(
    modifier: Modifier = Modifier,
    leading: @Composable () -> Unit,
    titleRow: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    onClick: (() -> Unit)? = null,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = 0.dp,
        backgroundColor = colors.default.input,
        shape = RoundedCornerShape(dimensions.cornerRadius),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensions.cornerRadius))
                .then(
                    if (onClick != null)
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(),
                            onClick = onClick
                        )
                    else Modifier
                )
                .padding(vertical = dimensions.x8)
                .padding(horizontal = dimensions.x8 + dimensions.x2),
            horizontalArrangement = Arrangement.spacedBy(dimensions.x8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leading()

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(3.5.dp),
            ) {
                titleRow()
                subtitle()
            }
        }
    }
}
