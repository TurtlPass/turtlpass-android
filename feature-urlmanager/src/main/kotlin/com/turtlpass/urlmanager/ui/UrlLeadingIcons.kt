package com.turtlpass.urlmanager.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.turtlpass.appmanager.ui.AndroidAppImage

@Composable
fun UrlLeadingIcons(
    faviconUrl: String,
    packageName: String,
) {
    Box(modifier = Modifier.size(42.dp)) {
        Box(
            modifier = Modifier.size(36.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            FaviconImage(
                modifier = Modifier.size(28.dp),
                url = faviconUrl
            )
        }

        AndroidAppImage(
            modifier = Modifier
                .requiredSize(16.dp)
                .align(Alignment.BottomEnd),
            packageName = packageName,
            placeholderPadding = true
        )
    }
}
