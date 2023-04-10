package com.example.data.model.object_tranfer_socket

import kotlinx.serialization.Serializable

@Serializable
data class WebRTCCallingCommand(
    val chatId: String,
    val commandCalling: String
)