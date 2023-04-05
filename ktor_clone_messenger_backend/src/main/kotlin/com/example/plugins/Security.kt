package com.example.plugins

import com.example.session.MessengerSession
import io.ktor.server.sessions.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.*

fun Application.configureSecurity() {
    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MessengerSession>("SESSION") {
        }
    }

    intercept(ApplicationCallPipeline.Features) {
        if (call.sessions.get<MessengerSession>() == null) {
            val username = call.parameters["username"] ?: "Guest"
            call.sessions.set(MessengerSession(username, generateNonce()))
        }
    }
//    routing {
//        get("/session/increment") {
//                val session = call.sessions.get<MySession>() ?: MySession()
//                call.sessions.set(session.copy(count = session.count + 1))
//                call.respondText("Counter is ${session.count}. Refresh to increment.")
//            }
//    }
}
