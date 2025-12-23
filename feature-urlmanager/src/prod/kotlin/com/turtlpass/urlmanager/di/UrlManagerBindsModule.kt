package com.turtlpass.urlmanager.di

import com.turtlpass.urlmanager.usecase.WebsiteUpdatesUseCase
import com.turtlpass.urlmanager.usecase.WebsiteUpdatesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UrlManagerBindsModule {

    @Binds
    abstract fun bindWebsiteUpdatesUseCase(impl: WebsiteUpdatesUseCaseImpl): WebsiteUpdatesUseCase
}
