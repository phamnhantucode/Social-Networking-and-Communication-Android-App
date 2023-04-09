package com.example.data.model.chat

import com.example.data.model.object_tranfer_socket.MessageTransfer
import com.example.data.model.user.User
import org.bson.codecs.pojo.annotations.BsonId

interface ChatDataSource {
    suspend fun getAllChats(userId: String): List<Chat>

    suspend fun insertChat(chat: Chat)

    suspend fun getChat(@BsonId id: String): Chat?

    suspend fun insertMessage(chat: Chat, message: MessageTransfer): Chat

    suspend fun getAllMessages(chatId: String): List<Message>?

}