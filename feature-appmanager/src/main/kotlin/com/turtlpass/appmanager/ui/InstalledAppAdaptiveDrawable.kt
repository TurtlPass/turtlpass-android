package com.turtlpass.appmanager.ui

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.memory.MemoryCache
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.turtlpass.appmanager.fetcher.adaptiveIcon.background.AppIconBackground
import com.turtlpass.appmanager.fetcher.adaptiveIcon.foreground.AppIconForeground
import com.turtlpass.ui.anim.shimmerOverContent
import com.turtlpass.ui.icons.Turtlpass24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.Grey200

@Composable
fun InstalledAppAdaptiveDrawable(
    modifier: Modifier = Modifier,
    packageName: String,
    shimmerEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    val context = LocalContext.current

    val foregroundKey = packageName + "_foreground"
    val foregroundModel = remember(foregroundKey) {
        ImageRequest.Builder(context)
            .data(AppIconForeground(packageName)) // handled by AppIconForegroundFetcher
            .memoryCacheKey(MemoryCache.Key("$packageName:fg"))
            .diskCacheKey("$packageName:fg")
            .crossfade(true)
            .build()
    }
    val backgroundKey = packageName + "_background"
    val backgroundModel = remember(backgroundKey) {
        ImageRequest.Builder(context)
            .data(
                AppIconBackground(
                    packageName = packageName,
                    fallbackColor = Grey200.toArgb()
                )
            ) // handled by AppIconBackgroundFetcher
            .memoryCacheKey(MemoryCache.Key("$packageName:bg"))
            .diskCacheKey("$packageName:bg")
            .crossfade(true)
            .build()
    }

    var isForegroundLoading by rememberSaveable(foregroundModel) { mutableStateOf(true) }
    var isForegroundError by remember { mutableStateOf(false) }
    var isBackgroundLoading by rememberSaveable(backgroundModel) { mutableStateOf(true) }

    Box(
        modifier = modifier
            .padding(top = dimensions.x8)
            .padding(horizontal = dimensions.x8)
            .clip(RoundedCornerShape(dimensions.cornerRadius))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick
            )
            .border(
                width = 1.dp,
                color = Grey200,
                shape = RoundedCornerShape(dimensions.cornerRadius),
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .matchParentSize()
                .then(
                    if (shimmerEnabled && (isForegroundLoading || isBackgroundLoading))
                        Modifier.shimmerOverContent(
                            highlightColor = colors.default.placeholderHighlight
                        )
                    else Modifier
                ),
        )
        AsyncImage(
            modifier = modifier
                .clip(RoundedCornerShape(corner = CornerSize(dimensions.cornerRadius)))
                .matchParentSize(),
            model = backgroundModel,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            onLoading = { isBackgroundLoading = true },
            onSuccess = { isBackgroundLoading = false },
            onError = { isBackgroundLoading = false }
        )
        AsyncImage(
            modifier = modifier
                .padding(horizontal = dimensions.x8)
                .fillMaxWidth(),
            model = foregroundModel,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            onLoading = { isForegroundLoading = true },
            onSuccess = { isForegroundLoading = false },
            onError = { isForegroundLoading = false; isForegroundError = true }
        )
        if (isForegroundLoading || isForegroundError) {
            Icon(
                modifier = Modifier.size(64.dp),
                imageVector = Turtlpass24Px,
                contentDescription = null,
                tint = Grey200,
            )
        }
    }
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
        InstalledAppAdaptiveDrawable(
            modifier = Modifier
                .width(125.dp)
                .requiredHeight(140.dp),
            packageName = "com.turtlpass",
            onClick = {}
        )
    }
}
