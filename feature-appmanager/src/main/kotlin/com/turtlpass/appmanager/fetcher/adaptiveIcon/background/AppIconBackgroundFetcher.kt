package com.turtlpass.appmanager.fetcher.adaptiveIcon.background

import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.ColorDrawable
import coil3.asImage
import coil3.decode.DataSource
import coil3.fetch.FetchResult
import coil3.fetch.Fetcher
import coil3.fetch.ImageFetchResult
import com.turtlpass.appmanager.fetcher.extension.getAppBackgroundColorPallet
import com.turtlpass.appmanager.fetcher.extension.isDrawableFullyTransparent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppIconBackgroundFetcher(
    private val packageManager: PackageManager,
    private val packageName: String,
    private val fallbackColor: Int
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val drawable = withContext(Dispatchers.IO) {
            try {
                val icon = packageManager.getApplicationIcon(packageName)
                val adaptive = icon as? AdaptiveIconDrawable
                val background = adaptive?.background

                when {
                    background == null -> null
                    background is ColorDrawable && background.color == Color.TRANSPARENT -> null
                    isDrawableFullyTransparent(background) -> null
                    else -> background
                }
            } catch (_: Exception) {
                null
            }
        } ?: ColorDrawable(
            getAppBackgroundColorPallet(packageManager, packageName, fallbackColor)
        )

        return ImageFetchResult(
            image = drawable.asImage(shareable = true),
            isSampled = false,
            dataSource = DataSource.MEMORY
        )
    }
}
