package com.turtlpass.appmanager.di

import com.turtlpass.appmanager.usecase.InstalledAppsUseCase
import com.turtlpass.appmanager.usecase.InstalledAppsUseCaseImpl
import com.turtlpass.appmanager.usecase.UsageAccessUseCaseImpl
import com.turtlpass.appmanager.usecase.RecentAppsUsageUseCaseImpl
import com.turtlpass.appmanager.usecase.RecentAppsUsageUseCase
import com.turtlpass.appmanager.usecase.UsageAccessUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppManagerBindsModule {

    @Binds
    abstract fun bindInstalledAppsUseCase(impl: InstalledAppsUseCaseImpl): InstalledAppsUseCase

    @Binds
    abstract fun bindUsageAccessUseCase(impl: UsageAccessUseCaseImpl): UsageAccessUseCase

    @Binds
    abstract fun bindRecentAppsUsageUseCase(impl: RecentAppsUsageUseCaseImpl): RecentAppsUsageUseCase
}
