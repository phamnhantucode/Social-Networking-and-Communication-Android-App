package com.phamnhantucode.composeclonemessengerclient.chatfeature.data

import com.example.data.model.object_tranfer_socket.Command
import com.example.data.model.object_tranfer_socket.DataInitChat
import com.example.data.model.object_tranfer_socket.MessageTransfer
import com.phamnhantucode.composeclonemessengerclient.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {
    suspend fun initWebSocketSession(): Resource<Unit>

    suspend fun send(command: Command)

    suspend fun observe(): Flow<Command>

    suspend fun initChat(dataInitChat: DataInitChat)

    suspend fun closeSession()
}