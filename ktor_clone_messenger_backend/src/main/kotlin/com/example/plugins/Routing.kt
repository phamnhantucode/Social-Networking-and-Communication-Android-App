package com.example.plugins

import com.example.controller.ChatController
import com.example.controller.LoginController
import com.example.controller.MainController
import com.example.routes.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import java.io.File

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

    //routing static
    routing {
        static("/files") {
            staticRootFolder = File("files")
            files(".")
        }
    }
    routing {
        get("/download") {
//            val file = File("files/ktor_logo.png")
//            call.response.header(
//                HttpHeaders.ContentDisposition,
//                ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "ktor_logo.png")
//                    .toString()
//            )
//            call.respondFile(file)
        }
    }
}
