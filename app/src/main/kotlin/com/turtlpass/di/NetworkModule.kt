package com.turtlpass.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.turtlpass.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context,
    ): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context)
            .collector(
                ChuckerCollector(
                    context = context,
                    showNotification = true,
                    retentionPeriod = RetentionManager.Period.ONE_HOUR
                )
            )
            // Read the whole response body even when the client does not consume the response completely.
            // This is useful in case of parsing errors or when the response body
            // is closed before being read like in Retrofit with Void and Unit types.
            .alwaysReadResponseBody(true)
            .build()
    }

    @Provides
    fun provideOkHttpClientBuilder(
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient.Builder {
        return OkHttpClient().newBuilder().apply {
            // timeout in making the initial connection; i.e. completing the TCP connection handshake
            connectTimeout(BuildConfig.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            // timeout on waiting to read data
            readTimeout(BuildConfig.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            retryOnConnectionFailure(true)
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor { message -> Timber.i(message) }.apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                addInterceptor(chuckerInterceptor)
            }
        }
    }
}
