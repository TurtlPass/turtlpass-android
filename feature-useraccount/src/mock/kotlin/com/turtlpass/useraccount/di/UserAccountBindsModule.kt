package com.turtlpass.useraccount.di

import com.turtlpass.useraccount.usecase.MockUserAccountsUseCaseImpl
import com.turtlpass.useraccount.usecase.UserAccountsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserAccountBindsModule {

    @Binds
    abstract fun bindUserAccountsUseCase(impl: MockUserAccountsUseCaseImpl): UserAccountsUseCase
}
