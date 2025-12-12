package com.turtlpass.appmanager.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.memory.MemoryCache
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.placeholder
import com.eygraber.compose.placeholder.shimmer
import com.turtlpass.appmanager.fetcher.icon.AppIcon
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.ui.icons.Apps24Px
import com.turtlpass.ui.tintedVector.rememberTintedVectorPainter
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors

@Composable
fun InstalledAppImage(
    modifier: Modifier = Modifier,
    installedApp: InstalledAppUi?,
    showShimmer: Boolean,
) {
    val context = LocalContext.current
    val model = remember(installedApp?.packageName) {
        installedApp?.let { app ->
            ImageRequest.Builder(context)
                .data(AppIcon(app.packageName)) // handled by AppIconFetcher
                .memoryCacheKey(MemoryCache.Key(app.packageName))
                .diskCacheKey(app.packageName)
                .crossfade(true)
                .build()
        }
    }
    var isLoading by rememberSaveable(model) { mutableStateOf(true) }
    AsyncImage(
        modifier = modifier.placeholder(
            visible = showShimmer && isLoading,
            color = colors.default.placeholder,
            highlight = PlaceholderHighlight.shimmer(colors.default.placeholderHighlight),
            shape = CircleShape
        ),
        model = model,
        placeholder = if (showShimmer) null else rememberTintedVectorPainter(Apps24Px, colors.default.icon),
        error = rememberTintedVectorPainter(Apps24Px, colors.default.icon),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        onLoading = { isLoading = true },
        onSuccess = { isLoading = false },
        onError = { isLoading = false },
    )
}

@OptIn(ExperimentalMaterialApi::class)
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
        InstalledAppImage(
            modifier = Modifier
                .requiredSize(48.dp),
            installedApp = InstalledAppUi("TurtlPass", "com.turtlpass", "domain"),
            showShimmer = false
        )
    }
}
