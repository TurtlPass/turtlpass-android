package com.turtlpass.module.installedapp.ui

import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.turtlpass.R
import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import timber.log.Timber

@Composable
fun InstalledAppImage(
    modifier: Modifier = Modifier,
    installedApp: InstalledApp?,
    showShimmer: Boolean,
) {
    val context = LocalContext.current
    val imageRequest = installedApp?.let { app ->
        ImageRequest.Builder(context)
            .data(context.packageManager.getAppDrawable(app.packageName))
            .memoryCacheKey(MemoryCache.Key(app.packageName))
            .diskCacheKey(app.packageName)
            .crossfade(true)
            .build()
    }
    val model = remember(installedApp?.packageName) { imageRequest }
    var isLoading by rememberSaveable(model) { mutableStateOf(true) }
    AsyncImage(
        modifier = modifier.placeholder(
            visible = showShimmer && isLoading,
            color = colors.default.placeholder,
            highlight = PlaceholderHighlight.shimmer(colors.default.placeholderHighlight),
            shape = CircleShape
        ),
        model = model,
        placeholder = if (showShimmer) null else painterResource(R.drawable.ic_apps),
        error = painterResource(R.drawable.ic_apps),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        onLoading = { isLoading = true },
        onSuccess = { isLoading = false },
        onError = { isLoading = false },
    )
}

private fun PackageManager.getAppDrawable(packageName: String): Drawable? {
    var drawable: Drawable? = null
    try {
        drawable = getApplicationIcon(packageName)
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.e(e)
    }
    return drawable
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
        InstalledAppImage(
            modifier = Modifier
                .requiredSize(48.dp),
            installedApp = InstalledApp("TurtlPass", "com.turtlpass", "domain"),
            showShimmer = false
        )
    }
}
