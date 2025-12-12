package com.turtlpass.urlmanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turtlpass.ui.theme.AppTheme.typography

@Composable
fun UrlTitleRow(
    url: String,
    time: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = url,
                style = typography.title.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            modifier = Modifier.padding(end = 6.dp),
            text = time,
            style = typography.subtitle,
        )
    }
}
