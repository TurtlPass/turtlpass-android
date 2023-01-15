package com.turtlpass.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.turtlpass.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {

    @Provides
    @Singleton
    fun provideImageLoader(
        context: Context,
        okHttpClient: OkHttpClient,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .logger(if (BuildConfig.DEBUG) DebugLogger() else null)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.9)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.9)
                    .build()
            }
            .okHttpClient { okHttpClient }
            .networkCachePolicy(CachePolicy.ENABLED)
            .networkObserverEnabled(false)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .fetcherDispatcher(dispatcher)
            .decoderDispatcher(dispatcher)
            .interceptorDispatcher(dispatcher)
            .transformationDispatcher(dispatcher)
            .build()
    }
}
