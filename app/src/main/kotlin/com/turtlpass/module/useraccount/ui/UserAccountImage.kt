package com.turtlpass.module.useraccount.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.google.accompanist.placeholder.placeholder
import com.turtlpass.R
import com.turtlpass.module.useraccount.model.UserAccount
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors

@Composable
fun UserAccountImage(
    modifier: Modifier = Modifier,
    userAccount: UserAccount?,
    showPlaceholder: Boolean,
) {
    val context = LocalContext.current
    val imageRequest = userAccount?.let { account ->
        with(ImageRequest.Builder(context)) {
            if (account.thumbnailUri != null) {
                data(account.thumbnailUri)
            } else {
                data(account.gravatarUrl)
            }
            memoryCacheKey(MemoryCache.Key(account.accountId))
            diskCacheKey(account.accountId)
            crossfade(true)
            build()
        }
    }
    val model = remember(userAccount?.accountId) { imageRequest }
    var isLoading by rememberSaveable(model) {
        mutableStateOf(false)
    }
    AsyncImage(
        modifier = modifier
            .placeholder(
                visible = showPlaceholder && isLoading,
                color = colors.default.placeholder,
                shape = CircleShape
            )
            .clip(CircleShape),
        model = model,
        placeholder = if (showPlaceholder) null else painterResource(R.drawable.ic_account),
        error = painterResource(R.drawable.ic_account),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        onLoading = { isLoading = true },
        onSuccess = { isLoading = false },
        onError = { isLoading = false },
    )
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
        UserAccountImage(
            modifier = Modifier
                .requiredSize(48.dp),
            userAccount = UserAccount("android@turtlpass.com", null),
            showPlaceholder = false
        )
    }
}
