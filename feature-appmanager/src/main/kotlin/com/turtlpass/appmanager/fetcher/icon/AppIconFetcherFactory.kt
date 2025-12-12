package com.turtlpass.appmanager.fetcher.icon

import android.content.pm.PackageManager
import coil3.ImageLoader
import coil3.fetch.Fetcher
import coil3.request.Options

class AppIconFetcherFactory(
    private val packageManager: PackageManager
) : Fetcher.Factory<AppIcon> {

    override fun create(data: AppIcon, options: Options, imageLoader: ImageLoader): Fetcher {
        return AppIconFetcher(packageManager, data.packageName)
    }
}
