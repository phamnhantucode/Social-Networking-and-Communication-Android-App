package com.example.data.model.chat

import com.example.data.model.object_tranfer_socket.MessageTransfer
import com.example.data.model.user.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class ChatDataSourceImpl(
    private val db: CoroutineDatabase

) : ChatDataSource {

    private val chats = db.getCollection<Chat>("chat")
    override suspend fun getAllChats(userId: String): List<Chat> {
        return chats.find().toList().filter {
            if (
                it.participants.find { it.id == userId }
                != null
            )
                true
            else false
        }
    }

    override suspend fun insertChat(chat: Chat) {
        chats.insertOne(chat)
    }

    override suspend fun getChat(id: String): Chat? {

        return chats.findOne(Chat::id eq id)
    }

    override suspend fun insertMessage(chat: Chat, message: MessageTransfer): Chat {
        val message = Message(
            content = message.text,
            senderId = message.senderId,
            createAt = message.createAt,
            updateAt = message.createAt
        )
        chat.messages.add(0, message)
        chats.updateOne(Chat::id eq chat.id, setValue(
            Chat::messages,
            chat.messages
        ))
        return chat
    }

    override suspend fun getAllMessages(chatId: String): List<Message>? {
        val chat = chats.findOne(Chat::id eq chatId)
        return chat?.let {
            return it.messages as List<Message>
        }
    }
}