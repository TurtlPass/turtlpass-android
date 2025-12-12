package com.turtlpass.appmanager.fetcher.adaptiveIcon.foreground

import android.content.pm.PackageManager
import android.graphics.drawable.AdaptiveIconDrawable
import coil3.asImage
import coil3.decode.DataSource
import coil3.fetch.FetchResult
import coil3.fetch.Fetcher
import coil3.fetch.ImageFetchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppIconForegroundFetcher(
    private val packageManager: PackageManager,
    private val packageName: String
) : Fetcher {

    override suspend fun fetch(): FetchResult? {
        val drawable = withContext(Dispatchers.IO) {
            try {
                val icon = packageManager.getApplicationIcon(packageName)
                (icon as? AdaptiveIconDrawable)?.foreground ?: icon
            } catch (_: Exception) {
                null
            }
        } ?: return null

        return ImageFetchResult(
            image = drawable.asImage(shareable = true),
            isSampled = false,
            dataSource = DataSource.MEMORY
        )
    }
}
