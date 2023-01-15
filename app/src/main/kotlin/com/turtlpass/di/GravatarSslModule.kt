package com.turtlpass.di

import com.turtlpass.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import java.net.URL
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GravatarSslModule {

    /**
     * Check certificate chain:
     *   $ openssl s_client -showcerts -servername gravatar.com -connect gravatar.com:443
     *
     * SSL-Session:
     *      Protocol:    TLSv1.2
     *      Cipher:      ECDHE-RSA-CHACHA20-POLY1305
     */
    @Provides
    @Singleton
    fun provideConnectionSpec(): ConnectionSpec {
        return ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256)
            .build()
    }

    /**
     * Pin certificate (hash of a certificate's Subject Public Key) for given host name
     *   $ openssl s_client -servername gravatar.com -connect gravatar.com:443 | openssl x509 -pubkey -noout | openssl rsa -pubin -outform der | openssl dgst -sha256 -binary | openssl enc -base64
     */
    @Provides
    @Singleton
    fun provideCertificatePinner(): CertificatePinner {
        val hostname = URL(BuildConfig.GRAVATAR_BASE_URL).host
        return CertificatePinner.Builder()
            .add(hostname, BuildConfig.GRAVATAR_PIN_SET)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        builder: OkHttpClient.Builder,
        connectionSpec: ConnectionSpec,
        certificatePinner: CertificatePinner
    ): OkHttpClient = builder.apply {
        connectionSpecs(Collections.singletonList(connectionSpec))
        certificatePinner(certificatePinner)
        hostnameVerifier { hostname, _ ->
            hostname == URL(BuildConfig.GRAVATAR_BASE_URL).host
        }
    }.build()
}
