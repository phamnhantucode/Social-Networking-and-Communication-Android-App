package com.example

import com.example.di.mainModule
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.partialcontent.*
import com.example.plugins.*
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Koin) {
        modules(mainModule)
    }
    install(PartialContent)
    install(AutoHeadResponse)
//    install(StaticContent) {
//        static("/images") {
//            resources("images")
//        }
//    }
    configureSockets()
    configureMonitoring()
    configureSerialization()
    configureSecurity()
    configureRouting()

}
