package com.example.di

import com.example.controller.ChatController
import com.example.controller.LoginController
import com.example.controller.MainController
import com.example.data.model.chat.ChatDataSource
import com.example.data.model.chat.ChatDataSourceImpl
import com.example.data.model.image.ImageDataSource
import com.example.data.model.image.ImageDataSourceImpl
import com.example.data.model.user.UserDataSource
import com.example.data.model.user.UserDataSourceImpl
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val mainModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("clone_messenger")
    }

    single<ImageDataSource> {
        ImageDataSourceImpl(get())
    }

    single<UserDataSource> {
        UserDataSourceImpl(get())
    }
    single<ChatDataSource> {
        ChatDataSourceImpl(get())
    }

    single<LoginController> {
        LoginController(get())
    }
    single<ChatController> {
        ChatController(get(), get())
    }

    single {
        MainController(
            get()
        )
    }
}