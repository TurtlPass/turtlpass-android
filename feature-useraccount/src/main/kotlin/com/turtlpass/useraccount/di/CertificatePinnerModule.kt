package com.turtlpass.useraccount.di

import com.turtlpass.network.HostCertificatePinner
import com.turtlpass.useraccount.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object CertificatePinnerModule {

    @Provides
    @IntoSet
    fun provideGravatarPin(): HostCertificatePinner =
        HostCertificatePinner(BuildConfig.GRAVATAR_HOST, BuildConfig.GRAVATAR_PIN_SET)

    @Provides
    @IntoSet
    fun provideLiaraPin(): HostCertificatePinner =
        HostCertificatePinner(BuildConfig.AVATAR_LIARA_HOST, BuildConfig.AVATAR_LIARA_PIN_SET)
}
