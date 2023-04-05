package com.example.controller

import com.example.data.model.chat.Chat
import com.example.data.model.chat.ChatDataSource
import com.example.data.model.chat.Message
import com.example.data.model.object_tranfer_socket.Command
import com.example.data.model.object_tranfer_socket.DataInitChat
import com.example.data.model.object_tranfer_socket.Member
import com.example.data.model.object_tranfer_socket.MessageTransfer
import com.example.data.model.user.UserDataSource
import com.example.util.CommandType
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class ChatController(
    val chatDataSource: ChatDataSource,
    val userDataSource: UserDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()
    fun onJoin(
        userId: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if(members.containsKey(userId)) {
            throw MemberAlreadyExistsException()
        }
        members[userId] = Member(
            userId = userId,
            sessionId = sessionId,
            socket = socket
        )
    }

    suspend fun tryDisconnect(userId: String) {
        members[userId]?.socket?.close()
        if(members.containsKey(userId)) {
            members.remove(userId)
        }
    }


    suspend fun observerIncoming(flow: Flow<Command>) {
        flow.onEach {command ->
            when (command.command) {
                CommandType.INIT_CHAT.command -> {
                    initChat(command.data)
                }
                CommandType.SEND_MESSAGE.command -> {
                    sendMessage(command.data)
                }
            }
        }
    }

    suspend fun initChat(data: String) {
        val dataInitChat = Json.decodeFromString<DataInitChat>(data)
        val participants = dataInitChat!!.participants!!.map {id ->
            userDataSource.getUser(id)!!
        }

        val chat = Chat(
            participants = participants,
            messages = dataInitChat.message?.let { listOf(it) as MutableList<Message> } ?: (listOf<Message>() as MutableList<Message>),
            createAt = dataInitChat.timeSend,
            updateAt = dataInitChat.timeSend
        )
        chatDataSource.insertChat(chat)
    }

    suspend fun sendMessage(data: String) {
        val messageTranfer = Json.decodeFromString<MessageTransfer>(data)
        val chat = chatDataSource.getChat(messageTranfer.chatId)
        val message = chat?.let { chatDataSource.insertMessage(it, messageTranfer) }
        message?.let {
            chat?.participants?.forEach {
                members[it.id]?.socket?.send(
                    Frame.Text(
                        Json.encodeToString(
                            Command.serializer(),
                            Command(
                                command = CommandType.RECEIVE_MESSAGE.command,
                                data = Json.encodeToString(
                                    Message.serializer(),
                                    message
                                )
                            )
                        )
                    )
                )
            }
        }
    }

}


class MemberAlreadyExistsException: Exception("Member already exists")