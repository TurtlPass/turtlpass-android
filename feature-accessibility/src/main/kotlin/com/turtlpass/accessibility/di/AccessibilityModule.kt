package com.turtlpass.accessibility.di

import android.content.Context
import android.view.accessibility.AccessibilityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AccessibilityModule {

    @Provides
    fun provideAccessibilityManager(context: Context): AccessibilityManager =
        context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
}
