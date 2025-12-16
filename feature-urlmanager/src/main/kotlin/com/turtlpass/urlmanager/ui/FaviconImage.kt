package com.turtlpass.urlmanager.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
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
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.turtlpass.ui.anim.shimmerOverContent
import com.turtlpass.ui.icons.Globe24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors

@Composable
fun FaviconImage(
    modifier: Modifier = Modifier,
    url: String,
    shimmerEnabled: Boolean = true,
) {
    val context = LocalContext.current
    val model = remember(url) {
        ImageRequest.Builder(context)
            .data(url)
            .memoryCacheKey(url)
            .diskCacheKey(url)
            .crossfade(true)
            .build()
    }

    var isLoading by rememberSaveable(url) { mutableStateOf(true) }
    var isError by rememberSaveable(url) { mutableStateOf(false) }

    Box(
        modifier = modifier
            .then(
                if (shimmerEnabled && isLoading)
                    Modifier
                        .clip(CircleShape)
                        .shimmerOverContent(
                            highlightColor = colors.default.placeholderHighlight
                        )
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (url.isNotEmpty()) {
            AsyncImage(
                model = model,
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize(),
                contentScale = ContentScale.FillBounds,
                onLoading = { isLoading = true },
                onSuccess = { isLoading = false; isError = false },
                onError = { isLoading = false; isError = true },
            )
        }
        if (isLoading || isError) {
            Icon(
                modifier = Modifier
                    .matchParentSize(),
                imageVector = Globe24Px,
                contentDescription = null,
                tint = colors.default.icon,
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
private fun PreviewFaviconImage() {
    AppTheme {
        FaviconImage(
            modifier = Modifier
                .requiredSize(48.dp),
            url = "turtlpass.com",
        )
    }
}
