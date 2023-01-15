package com.turtlpass.module.installedapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.theme.AppRipple
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography

@ExperimentalMaterialApi
@Composable
fun InstalledAppRow(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    app: InstalledApp,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(
        LocalRippleTheme provides AppRipple
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(end = dimensions.x16)
                .clip(shape = RoundedCornerShape(dimensions.cornerRadius))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(),
                    onClick = { if (lazyListState.isScrollInProgress.not()) onClick() }
                )
                .padding(start = dimensions.x8 + dimensions.x4)
                .padding(vertical = dimensions.x4)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InstalledAppImage(
                modifier = Modifier
                    .requiredSize(46.dp),
                installedApp = app,
                showShimmer = true
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = dimensions.x16),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = app.appName,
                    style = typography.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = domainTextHighlighted(app),
                    style = typography.subtitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun domainTextHighlighted(app: InstalledApp): AnnotatedString {
    val text = app.packageName
    val highlight = app.topLevelDomain
    if (highlight.isNotEmpty() && highlight.length < text.length) {
        val start = text.indexOf(highlight)
        if (start != -1) {
            return AnnotatedString(
                text = text, spanStyles = listOf(
                    AnnotatedString.Range(
                        SpanStyle(fontWeight = FontWeight.SemiBold),
                        start = start,
                        end = start + highlight.length
                    )
                )
            )
        }
    }
    return AnnotatedString(text = text)
}

@ExperimentalMaterialApi
@Preview(
    name = "Light theme",
    showBackground = true,
    backgroundColor = 0xffffffff,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Preview(
    name = "Dark theme",
    showBackground = true,
    backgroundColor = 0xff424242,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Composable
private fun Preview() {
    AppTheme {
        InstalledAppRow(
            lazyListState = rememberLazyListState(),
            app = InstalledApp("TurtlPass", "com.turtlpass", "domain"),
            onClick = {}
        )
    }
}
