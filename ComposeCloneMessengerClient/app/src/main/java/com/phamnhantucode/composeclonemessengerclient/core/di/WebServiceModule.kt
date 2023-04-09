package com.phamnhantucode.composeclonemessengerclient.core.di

import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatService
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatServiceImpl
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatSocketService
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatSocketServiceImpl
import com.phamnhantucode.composeclonemessengerclient.loginfeature.data.LoginService
import com.phamnhantucode.composeclonemessengerclient.loginfeature.data.LoginServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WebServiceModule {

    @Binds
    @Singleton
    abstract fun bindLoginService(
        loginService: LoginServiceImpl
    ): LoginService

    @Binds
    @Singleton
    abstract fun bindChatService(
        chatService: ChatServiceImpl
    ): ChatService

    @Binds
    @Singleton
    abstract fun bindChatSocketService(
        chatSocketService: ChatSocketServiceImpl
    ): ChatSocketService
}