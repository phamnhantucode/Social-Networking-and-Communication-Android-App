package com.phamnhantucode.composeclonemessengerclient.chatfeature

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.data.model.object_tranfer_socket.Command
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

object SaveStateVM {
    val _chatState = mutableStateOf(ChatState())
    val chatState: State<ChatState> = _chatState

    val messageTf = mutableStateOf("")
    var _currentMessageState = mutableStateOf(ChatDto())
    val currentMessageState: State<ChatDto> = _currentMessageState

    var _sessionCommandFlow: Flow<Command>? = null

    var connectedSocket = false
    var onCalling = false
}