package com.phamnhantucode.composeclonemessengerclient.chatfeature

import androidx.compose.runtime.MutableState
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatDto


data class ChatState(
    val chats: List<MutableState<ChatDto>> = emptyList(),
)
