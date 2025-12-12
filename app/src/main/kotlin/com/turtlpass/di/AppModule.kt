package com.turtlpass.di

import android.app.Application
import android.content.Context
import com.turtlpass.di.moduleNavigation.ActivityClass
import com.turtlpass.module.moduleNavigation.buildActivityClassList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideActivityClassList(): List<ActivityClass> = buildActivityClassList()
}
