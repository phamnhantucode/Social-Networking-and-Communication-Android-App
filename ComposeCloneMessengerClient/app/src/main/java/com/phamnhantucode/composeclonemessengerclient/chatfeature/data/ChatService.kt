package com.phamnhantucode.composeclonemessengerclient.chatfeature.data

import com.phamnhantucode.composeclonemessengerclient.core.util.Constant.BASE_URL

interface ChatService {

    suspend fun getALlMessages(chatId: String): List<MessageDto>

    suspend fun getAllChats(userId: String): List<ChatDto>

    sealed class Endpoints(val url: String) {
        object Chat: Endpoints("$BASE_URL/chat") {
            fun getAllChats(userId: String): String {
                return "$url/user/$userId"
            }
            fun getAllMessages(chatId: String): String {
                return "$url/$chatId"
            }
        }
    }
}