package com.turtlpass.network

import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkImageComponentsModule {

    @Provides
    @IntoSet
    fun provideNetworkFetcher(okHttpClient: OkHttpClient): ImageLoaderComponentContributor = {
        add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient }))
    }
}
