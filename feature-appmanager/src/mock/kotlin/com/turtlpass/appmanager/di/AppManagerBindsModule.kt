package com.turtlpass.appmanager.di

import com.turtlpass.appmanager.usecase.InstalledAppsUseCase
import com.turtlpass.appmanager.usecase.MockInstalledAppsUseCaseImpl
import com.turtlpass.appmanager.usecase.MockUsageAccessUseCaseImpl
import com.turtlpass.appmanager.usecase.MockRecentAppsUsageUseCaseImpl
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
    abstract fun bindInstalledAppsUseCase(impl: MockInstalledAppsUseCaseImpl): InstalledAppsUseCase

    @Binds
    abstract fun bindUsageAccessUseCase(impl: MockUsageAccessUseCaseImpl): UsageAccessUseCase

    @Binds
    abstract fun bindRecentAppsUsageUseCase(impl: MockRecentAppsUsageUseCaseImpl): RecentAppsUsageUseCase
}
