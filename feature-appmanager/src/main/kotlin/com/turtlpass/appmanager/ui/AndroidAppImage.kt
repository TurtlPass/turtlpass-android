package com.turtlpass.appmanager.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.memory.MemoryCache
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.turtlpass.appmanager.fetcher.icon.AppIcon
import com.turtlpass.ui.anim.shimmerOverContent
import com.turtlpass.ui.icons.Turtlpass24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.Grey200

@Composable
fun AndroidAppImage(
    modifier: Modifier = Modifier,
    packageName: String,
    shimmerEnabled: Boolean = true,
    placeholderPadding: Boolean = false,
) {
    val context = LocalContext.current
    val model = remember(packageName) {
        ImageRequest.Builder(context)
            .data(AppIcon(packageName)) // handled by AppIconFetcher
            .memoryCacheKey(MemoryCache.Key(packageName))
            .diskCacheKey(packageName)
            .crossfade(true)
            .build()
    }
    var isLoading by rememberSaveable(model) {
        mutableStateOf(true)
    }
    var isError by rememberSaveable(model) {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .then(
                if (shimmerEnabled && isLoading)
                    Modifier
                        .clip(CircleShape)
                        .background(colors.default.placeholder)
                        .shimmerOverContent(
                        highlightColor = colors.default.placeholderHighlight
                    )
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading || isError) {
            Icon(
                imageVector = Turtlpass24Px,
                contentDescription = null,
                tint = Grey200,
                modifier = modifier
                    .then(
                        if (placeholderPadding)
                            Modifier.padding(2.dp)
                        else Modifier
                    ),
            )
        }
        AsyncImage(
            modifier = Modifier.matchParentSize(),
            model = model,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            onLoading = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = { isLoading = false; isError = true }
        )
    }

//    SubcomposeAsyncImage(
//        model = model,
//        imageLoader = imageLoader,
//        contentDescription = null,
//        contentScale = ContentScale.Fit,
//        modifier = modifier.clip(CircleShape),
//        loading = {
//            if (shimmerEnabled) {
//                Box(
//                    Modifier
//                        .matchParentSize()
//                        .shimmerOverContent(highlightColor = colors.default.placeholderHighlight)
//                ) {
//                    Icon(
//                        imageVector = Turtlpass24Px,
//                        contentDescription = null,
//                        tint = Grey200,
//                        modifier = if (placeholderPadding) Modifier.padding(2.dp) else Modifier
//                    )
//                }
//            } else {
//                Icon(
//                    imageVector = Turtlpass24Px,
//                    contentDescription = null,
//                    tint = Grey200,
//                    modifier = if (placeholderPadding) Modifier.padding(2.dp) else Modifier
//                )
//            }
//        },
//        error = {
//            Icon(
//                imageVector = Turtlpass24Px,
//                contentDescription = null,
//                tint = Grey200,
//                modifier = if (placeholderPadding) Modifier.padding(2.dp) else Modifier
//            )
//        }
//    )
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
        AndroidAppImage(
            modifier = Modifier
                .requiredSize(48.dp),
            packageName = "com.turtlpass",
        )
    }
}
