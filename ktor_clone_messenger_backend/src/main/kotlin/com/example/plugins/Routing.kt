package com.example.plugins

import com.example.controller.ChatController
import com.example.controller.LoginController
import com.example.controller.MainController
import com.example.routes.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
    val loginController by inject<LoginController>()
    val chatController by inject<ChatController>()
    val mainController by inject<MainController>()
    routing {
        login(loginController)
        chat(chatController)
        image(mainController)
    }
}
