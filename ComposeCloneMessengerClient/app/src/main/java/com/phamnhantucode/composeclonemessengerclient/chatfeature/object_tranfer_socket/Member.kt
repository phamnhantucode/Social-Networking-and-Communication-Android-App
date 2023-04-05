package com.example.data.model.object_tranfer_socket

import io.ktor.websocket.*
import kotlinx.serialization.Serializable

data class Member(
    val userId: String,
    val sessionId: String,
    val socket: WebSocketSession
)

