package com.phamnhantucode.composeclonemessengerclient.chatfeature.data

import android.util.Log
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

class ChatServiceImpl @Inject constructor(
    val client: HttpClient
): ChatService {
    override suspend fun getALlMessages(chatId: String): List<MessageDto> {
        return try {
            client.get<List<MessageDto>>(ChatService.Endpoints.Chat.getAllMessages(chatId))
        } catch (e: Exception) {
            e.printStackTrace()
            listOf<MessageDto>()
        }
    }

    override suspend fun getAllChats(userId: String): List<ChatDto> {
        return try {
            client.get<List<ChatDto>>(ChatService.Endpoints.Chat.getAllChats(userId)).also {
                Log.e("ROO", it.toString())
            }

        } catch (e: Exception) {
            e.printStackTrace()
            listOf<ChatDto>()
        }
    }
}