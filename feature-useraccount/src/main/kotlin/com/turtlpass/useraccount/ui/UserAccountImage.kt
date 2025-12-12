package com.turtlpass.useraccount.ui

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
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
import com.turtlpass.ui.anim.shimmerOverContent
import com.turtlpass.ui.icons.Turtlpass24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.Grey200
import com.turtlpass.useraccount.model.UserAccount

@Composable
fun UserAccountImage(
    modifier: Modifier = Modifier,
    userAccount: UserAccount?,
    shimmerEnabled: Boolean = true,
) {
    val context = LocalContext.current
    val model = remember(userAccount?.accountId) {
        userAccount?.let { account ->
            with(ImageRequest.Builder(context)) {
                data(account.thumbnailUri ?: account.avatarUrl)
                memoryCacheKey(MemoryCache.Key(account.accountId))
                diskCacheKey(account.accountId)
                crossfade(true)
                build()
            }
        }
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
            .border(1.5.dp, colors.default.icon, CircleShape)
            .then(
                if (shimmerEnabled && isLoading)
                    Modifier.shimmerOverContent(
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
                tint = if (isLoading) Grey200 else colors.default.icon,
                modifier = modifier.padding(start = 6.dp, end = 6.dp, bottom = 2.dp)
            )
        }
        AsyncImage(
            modifier = Modifier.matchParentSize(),
            model = model,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            onLoading = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = { isLoading = false; isError = true }
        )
    }
}

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
        UserAccountImage(
            modifier = Modifier
                .requiredSize(48.dp),
            userAccount = UserAccount("android@turtlpass.com", null),
        )
    }
}
