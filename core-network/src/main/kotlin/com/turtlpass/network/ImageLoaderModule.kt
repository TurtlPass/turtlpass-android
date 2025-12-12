package com.turtlpass.network

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.util.DebugLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okio.Path.Companion.toOkioPath
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {

    @Provides
    @Singleton
    fun provideImageLoader(
        context: Context,
        contributors: Set<@JvmSuppressWildcards ImageLoaderComponentContributor>
    ): ImageLoader = ImageLoader.Builder(context)
        .logger(if (BuildConfig.DEBUG) DebugLogger() else null)
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context, 0.9)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache").toOkioPath())
                .maxSizePercent(0.9)
                .build()
        }
        .components {
            contributors.forEach { contributor ->
                contributor(this)
            }
        }
        .networkCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .build()
}
