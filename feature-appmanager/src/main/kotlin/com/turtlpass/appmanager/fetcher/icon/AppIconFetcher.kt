package com.turtlpass.appmanager.fetcher.icon

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import coil3.asImage
import coil3.decode.DataSource
import coil3.fetch.FetchResult
import coil3.fetch.Fetcher
import coil3.fetch.ImageFetchResult
import com.turtlpass.appmanager.fetcher.extension.getApplicationIconOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppIconFetcher(
    private val packageManager: PackageManager,
    private val packageName: String
) : Fetcher {

    override suspend fun fetch(): FetchResult? {
        val drawable: Drawable = withContext(Dispatchers.IO) {
            packageManager.getApplicationIconOrNull(packageName)
        } ?: return null

        return ImageFetchResult(
            image = drawable.asImage(shareable = true),
            isSampled = false,
            dataSource = DataSource.MEMORY
        )
    }
}
