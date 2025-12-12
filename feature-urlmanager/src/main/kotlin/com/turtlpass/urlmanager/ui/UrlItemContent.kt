package com.turtlpass.urlmanager.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.turtlpass.model.WebsiteUi
import com.turtlpass.ui.theme.AppTheme.typography

@Composable
fun UrlItemContent(
    modifier: Modifier = Modifier,
    websiteUi: WebsiteUi,
    onClick: () -> Unit,
) {
    UrlItemLayout(
        modifier = modifier,
        onClick = onClick,
        leading = {
            UrlLeadingIcons(
                faviconUrl = websiteUi.faviconUrl,
                packageName = websiteUi.packageName
            )
        },
        titleRow = {
            UrlTitleRow(
                url = websiteUi.url,
                time = websiteUi.time,
            )
        },
        subtitle = {
            Text(
                text = websiteUi.appName,
                style = typography.subtitle
            )
        }
    )
}
