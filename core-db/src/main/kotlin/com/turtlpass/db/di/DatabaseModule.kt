package com.turtlpass.db.di

import android.app.Application
import androidx.room.Room
import com.turtlpass.db.AppDatabase
import com.turtlpass.db.dao.WebsiteEventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideWebsiteDao(db: AppDatabase): WebsiteEventDao =
        db.websiteEventDao()
}
