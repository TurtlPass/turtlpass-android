package com.turtlpass.appmanager.fetcher.adaptiveIcon.background

import android.content.pm.PackageManager
import coil3.ImageLoader
import coil3.fetch.Fetcher
import coil3.request.Options

class AppIconBackgroundFetcherFactory(
    private val packageManager: PackageManager
) : Fetcher.Factory<AppIconBackground> {

    override fun create(
        data: AppIconBackground,
        options: Options,
        imageLoader: ImageLoader
    ): Fetcher {
        return AppIconBackgroundFetcher(
            packageManager = packageManager,
            packageName = data.packageName,
            fallbackColor = data.fallbackColor
        )
    }
}
