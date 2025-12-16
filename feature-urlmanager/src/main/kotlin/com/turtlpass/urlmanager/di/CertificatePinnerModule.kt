package com.turtlpass.urlmanager.di

import com.turtlpass.network.HostCertificatePinner
import com.turtlpass.urlmanager.BuildConfig
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
    fun provideGooglePin(): HostCertificatePinner =
        HostCertificatePinner(BuildConfig.GOOGLE_HOST, BuildConfig.GOOGLE_SPKI_PIN)

    @Provides
    @IntoSet
    fun provideGstaticPin(): HostCertificatePinner = // wildcard for subdomains
        HostCertificatePinner("*${BuildConfig.GSTATIC_HOST}", BuildConfig.GSTATIC_SPKI_PIN)
}
