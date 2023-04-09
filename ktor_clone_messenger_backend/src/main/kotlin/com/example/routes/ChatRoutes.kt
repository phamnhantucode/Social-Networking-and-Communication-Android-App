package com.example.routes

import com.example.controller.ChatController
import com.example.controller.LoginController
import com.example.controller.MainController
import com.example.controller.MemberAlreadyExistsException
import com.example.data.model.chat.Chat
import com.example.data.model.object_tranfer_socket.Command
import com.example.session.MessengerSession
import com.example.util.CommandType
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.login(loginController: LoginController) {
    post("/login") {
        try {
            val parameters = call.receiveParameters()

            val user = loginController.login(parameters["account"].toString(), parameters["password"].toString())

            call.sessions.set(MessengerSession(userId = user.id, generateNonce()))
            call.respond(user)
        } catch (e: Exception) {
            call.respondText(e.message.toString())
        }
    }

    post("/signup") {
        try {
            val parameters = call.receiveParameters()

            val user = loginController.signup(parameters["account"].toString(), parameters["password"].toString())
            call.sessions.set(MessengerSession(userId = user.id, generateNonce()))
            call.respond(user)
        } catch (e: Exception) {
            call.respondText(e.message.toString())
        }
    }
}

fun Route.chat(chatController: ChatController) {
    webSocket("/chat-socket") {

        var session = call.sessions.get<MessengerSession>()
        if(session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
            return@webSocket
//            call.sessions.set(MessengerSession("Guest", generateNonce()))
        }
//        session = call.sessions.get<MessengerSession>()
        try {
            chatController.onJoin(
                userId = session!!.userId,
                sessionId = session.sessionId,
                socket = this
            )

            chatController.observerIncoming(incoming
                .consumeAsFlow()
                .filter { it is Frame.Text }
                .map {frame ->
                    val command = Json.decodeFromString<Command>((frame as Frame.Text).readText())
                    command
                }
            )
        } catch(e: MemberAlreadyExistsException) {
            call.respond(HttpStatusCode.Conflict)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
//            chatController.tryDisconnect(session!!.userId)
        }

    }
    get("/chat/{chatId}") {
        val messages = chatController.getAllMessages(call.parameters["chatId"].toString())
        messages?.let {
            call.respond(
                messages
            )
        }
    }
    get("/chat/user/{userId}") {
        val chats = chatController.getAllChats(call.parameters["userId"].toString())
        chats?.let {
            call.respond(
                chats
            )
        }
    }
}

fun Route.image(mainController: MainController) {
    get("/image/{id}") {
            val image = mainController.getImage(call.parameters["id"].toString())

            image?.let {
                call.respond(
                    HttpStatusCode.OK,
                    it)
            }
    }
}


