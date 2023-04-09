package com.example.data.model.object_tranfer_socket

import io.ktor.http.cio.websocket.*
import kotlinx.serialization.Serializable

data class Member(
    val userId: String,
    val sessionId: String,
    val socket: WebSocketSession
)

