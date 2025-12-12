package com.turtlpass.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.CertificatePinner
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion

@Module
@InstallIn(SingletonComponent::class)
object SecureImageNetworkModule {

    /**
     * Check certificate chain:
     *   $ openssl s_client -showcerts -servername gravatar.com -connect gravatar.com:443
     *
     * SSL-Session:
     *      Protocol:    TLSv1.3
     *      Cipher:      TLS_AES_256_GCM_SHA384
     */
    @Provides
    @Singleton
    fun provideConnectionSpec(): ConnectionSpec {
        return ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_3)
            .cipherSuites(CipherSuite.TLS_AES_256_GCM_SHA384)
            .build()
    }

    /**
     * Pin certificate (hash of a certificate's Subject Public Key) for given host name
     *   $ openssl s_client -servername gravatar.com -connect gravatar.com:443 | openssl x509 -pubkey -noout | openssl rsa -pubin -outform der | openssl dgst -sha256 -binary | openssl enc -base64
     */
    @Provides
    @Singleton
    fun provideCertificatePinner(
        hostCertificatePinners: Set<@JvmSuppressWildcards HostCertificatePinner>
    ): CertificatePinner {
        val builder = CertificatePinner.Builder()
        hostCertificatePinners.filterNotNull().forEach { (host, pin) ->
            builder.add(host, pin)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        builder: OkHttpClient.Builder,
        connectionSpec: ConnectionSpec,
        certificatePinner: CertificatePinner,
        allowedHosts: Set<@JvmSuppressWildcards AllowedHostname>
    ): OkHttpClient {
        return builder.apply {
            connectionSpecs(listOf(connectionSpec))
            certificatePinner(certificatePinner)
            hostnameVerifier { hostname, _ ->
                allowedHosts.any { host ->
                    if (host.wildcard) {
                        hostname.endsWith(host.hostname)
                    } else {
                        hostname == host.hostname
                    }
                }
            }
        }.build()
    }
}
