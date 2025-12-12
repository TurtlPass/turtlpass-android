package com.turtlpass.urlmanager.di

import com.turtlpass.network.AllowedHostname
import com.turtlpass.urlmanager.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object AllowedHostnameModule {

    @Provides
    @IntoSet
    fun provideGoogleHost(): AllowedHostname =
        AllowedHostname(hostname = BuildConfig.GOOGLE_HOST, wildcard = false)

    @Provides
    @IntoSet
    fun provideGstaticHost(): AllowedHostname =
        AllowedHostname(hostname = BuildConfig.GSTATIC_HOST, wildcard = true)
}
