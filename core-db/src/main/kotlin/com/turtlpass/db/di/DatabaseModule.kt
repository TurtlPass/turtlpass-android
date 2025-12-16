package com.turtlpass.db.di

import android.app.Application
import androidx.room.Room
import com.turtlpass.db.AppDatabase
import com.turtlpass.db.dao.WebsiteEventDao
import com.turtlpass.db.keystore.DatabaseKeyStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        val passphrase = try {
            DatabaseKeyStore.getPassphrase(app)
        } catch (e: Exception) {
            Timber.e(e, "Keystore invalidated or corrupted encrypted key")
            app.deleteDatabase("app_db")
            DatabaseKeyStore.getPassphrase(app)
        }

        return Room.databaseBuilder(app, AppDatabase::class.java, "app_db")
            .openHelperFactory(SupportOpenHelperFactory(passphrase))
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideWebsiteDao(db: AppDatabase): WebsiteEventDao =
        db.websiteEventDao()
}
