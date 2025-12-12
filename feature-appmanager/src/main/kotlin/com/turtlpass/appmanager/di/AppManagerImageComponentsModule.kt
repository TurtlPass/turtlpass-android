package com.turtlpass.appmanager.di

import android.content.pm.PackageManager
import com.turtlpass.appmanager.fetcher.adaptiveIcon.background.AppIconBackgroundFetcherFactory
import com.turtlpass.appmanager.fetcher.adaptiveIcon.foreground.AppIconForegroundFetcherFactory
import com.turtlpass.appmanager.fetcher.icon.AppIconFetcherFactory
import com.turtlpass.network.ImageLoaderComponentContributor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object AppManagerImageComponentsModule {

    @Provides
    @IntoSet
    fun provideAppIconComponents(
        packageManager: PackageManager
    ): ImageLoaderComponentContributor = {
        add(AppIconFetcherFactory(packageManager))
        add(AppIconForegroundFetcherFactory(packageManager))
        add(AppIconBackgroundFetcherFactory(packageManager))
    }
}
