package com.turtlpass.useraccount.di

import com.turtlpass.network.AllowedHostname
import com.turtlpass.useraccount.BuildConfig
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
    fun provideGravatarHost(): AllowedHostname =
        AllowedHostname(hostname = BuildConfig.GRAVATAR_HOST, wildcard = false)

    @Provides
    @IntoSet
    fun provideLiaraHost(): AllowedHostname =
        AllowedHostname(hostname = BuildConfig.AVATAR_LIARA_HOST, wildcard = false)
}
