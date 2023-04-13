package com.phamnhantucode.composeclonemessengerclient.chatfeature

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatDto

object SaveStateVM {
    val _chatState = mutableStateOf(ChatState())
    val chatState: State<ChatState> = _chatState

    val messageTf = mutableStateOf("")
    var _currentMessageState = mutableStateOf(ChatDto())
    val currentMessageState: State<ChatDto> = _currentMessageState

    var connectedSocket = false
}