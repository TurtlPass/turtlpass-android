package com.turtlpass.urlmanager.di

import com.turtlpass.accessibility.usecase.AccessibilityEnabledUpdatesUseCase
import com.turtlpass.urlmanager.usecase.MockAccessibilityEnabledUpdatesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AccessibilityBindsModule {

    @Binds
    abstract fun bindAccessibilityEnabledUpdatesUseCase(impl: MockAccessibilityEnabledUpdatesUseCaseImpl): AccessibilityEnabledUpdatesUseCase
}
