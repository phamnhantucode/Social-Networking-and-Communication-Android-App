package com.phamnhantucode.composeclonemessengerclient.chatfeature.data

import com.example.data.model.object_tranfer_socket.Command
import com.example.data.model.object_tranfer_socket.CommandType
import com.example.data.model.object_tranfer_socket.DataInitChat
import com.example.data.model.object_tranfer_socket.MessageTransfer
import com.phamnhantucode.composeclonemessengerclient.core.util.Constant.BASE_URL
import com.phamnhantucode.composeclonemessengerclient.core.util.Resource
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ChatSocketServiceImpl @Inject constructor(
    val client: HttpClient
): ChatSocketService {
    private var socket: WebSocketSession? = null
    override suspend fun initWebSocketSession(): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url(BASE_URL + "/chat-socket")
            }
            if (socket?.isActive == true) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Couldn't establish connection")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.localizedMessage ?: "Unknow Message")
        }

    }

    override suspend fun send(command: Command) {
        try {
            socket?.send(Frame.Text(
                Json.encodeToString(
                    Command.serializer(),
                    command
                )
            ))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun observe(): Flow<Command> {
        return try {
            socket?.incoming?.receiveAsFlow()
                ?.filter {
                    it is Frame.Text
                }
                ?.map {
                    val command = Json.decodeFromString<Command>((it as Frame.Text).readText())
                    command
                } ?: flow{}
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            flow {}
        }
    }

    override suspend fun initChat(dataInitChat: DataInitChat) {
        try {
            socket?.send(Frame.Text(
                Json.encodeToString(
                    Command.serializer(),
                    Command(
                        CommandType.INIT_CHAT.command,
                        data = Json.encodeToString(
                            DataInitChat.serializer(),
                            dataInitChat
                        )
                    )
                )
            ))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }

}