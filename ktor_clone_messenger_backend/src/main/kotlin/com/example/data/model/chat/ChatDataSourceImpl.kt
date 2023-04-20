package com.example.data.model.chat

import com.example.SEPARATOR_MEDIA_STRING
import com.example.SEPARATOR_MULTIPLE_MEDIA_STRING
import com.example.data.model.image.Image
import com.example.data.model.image.ImageDataSource
import com.example.data.model.object_tranfer_socket.MessageTransfer
import org.apache.commons.io.FileUtils
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ChatDataSourceImpl(
    private val db: CoroutineDatabase,
    private val imageDataSource: ImageDataSource
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
        var content = ""
        when (MessageType.valueOf(message.type.uppercase())) {
            MessageType.TEXT -> {
                content = message.content
            }
            MessageType.CALL -> {
                content = message.content
            }
            MessageType.IMAGE -> {
                val imageExt = message.content.substringAfter("#")
                val image = Image(
                    url = chat.id + message.senderId + System.currentTimeMillis().toString() + "."+imageExt
                )
                val file = File("files/images/${image.url}")
                if (!file.exists()) {
                    file.createNewFile()
                }
                writeByteArrayToFile(file, message.content.substringBefore("#").encodeToByteArray())
                imageDataSource.insertImage(image)
                content = image.id
            }

            MessageType.IMAGES -> {
                print(message.content)
                val imagesByteArray = message.content.split(SEPARATOR_MULTIPLE_MEDIA_STRING)
                content = imagesByteArray.map {
                    val imageExt = it.substringAfter(SEPARATOR_MEDIA_STRING)
                    val image = Image(
                        url = chat.id + message.senderId + System.currentTimeMillis().toString() + "."+imageExt
                    )
//                    imageDataSource.insertImage(image)
                    val file = File("files/images/${image.url}")
                    println("files/images/${image.url}")
                    if (!file.exists()) {
                        file.createNewFile()
                    }
                    println("files/images/${image.url}")
                    writeByteArrayToFile(file, message.content.substringBefore("#").let { Base64.getDecoder().decode(it) })
                    imageDataSource.insertImage(image)
                    image.id
                }.joinToString(SEPARATOR_MULTIPLE_MEDIA_STRING)
                print(content)
            }
            MessageType.VIDEO -> {

            }
            MessageType.VIDEO_CALL -> {
                content = message.content
            }

        }
        val message = Message(
            content = content,
            senderId = message.senderId,
            createAt = message.createAt,
            updateAt = message.createAt,
            type = message.type
        )
        chat.messages.add(0, message)
        chats.updateOne(Chat::id eq chat.id, setValue(
            Chat::messages,
            chat.messages
        ))
        return chat
    }

    private fun writeByteArrayToFile(file: File, byteArray: ByteArray) {
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(byteArray)
    }

    override suspend fun getAllMessages(chatId: String): List<Message>? {
        val chat = chats.findOne(Chat::id eq chatId)
        return chat?.let {
            return it.messages as List<Message>
        }
    }
}