package com.turtlpass.appmanager.fetcher.adaptiveIcon.foreground

import android.content.pm.PackageManager
import coil3.ImageLoader
import coil3.fetch.Fetcher
import coil3.request.Options

class AppIconForegroundFetcherFactory(
    private val packageManager: PackageManager
) : Fetcher.Factory<AppIconForeground> {

    override fun create(
        data: AppIconForeground,
        options: Options,
        imageLoader: ImageLoader
    ): Fetcher {
        return AppIconForegroundFetcher(packageManager, data.packageName)
    }
}
