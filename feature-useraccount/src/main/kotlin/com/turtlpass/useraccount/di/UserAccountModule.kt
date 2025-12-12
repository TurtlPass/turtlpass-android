package com.turtlpass.useraccount.di

import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserAccountModule {

    @Provides
    fun provideAccountManager(context: Context): AccountManager = AccountManager.get(context)

    @Provides
    fun provideContentResolver(context: Context): ContentResolver = context.contentResolver
}
