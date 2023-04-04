package com.phamnhantucode.composeclonemessengerclient.core.di

import com.phamnhantucode.composeclonemessengerclient.loginfeature.LoginRepository
import com.phamnhantucode.composeclonemessengerclient.loginfeature.LoginRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        loginRepository: LoginRepositoryImpl
    ): LoginRepository


}