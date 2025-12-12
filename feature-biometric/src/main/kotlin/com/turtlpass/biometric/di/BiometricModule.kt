package com.turtlpass.biometric.di

import android.content.Context
import androidx.biometric.BiometricManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BiometricModule {

    @Provides
    fun provideBiometricManager(@ApplicationContext context: Context) =
        BiometricManager.from(context)
}
