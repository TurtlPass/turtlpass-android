package com.turtlpass.appmanager.di

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import com.turtlpass.appmanager.usecase.UsagePermissionProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppManagerModule {

    @Provides
    fun providePackageManager(context: Context): PackageManager = context.packageManager

    @Provides
    fun provideUsageStatsManager(context: Context): UsageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    @Provides
    fun provideAppOpsManager(context: Context): AppOpsManager =
        context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

    @Provides
    @Singleton
    fun providePermissionManager(@ApplicationContext context: Context) =
        UsagePermissionProvider(context)
}
