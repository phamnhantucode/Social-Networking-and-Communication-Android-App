package com.example.data.model.object_tranfer_socket

import kotlinx.serialization.Serializable

@Serializable
data class MessageTransfer (
    val chatId: String,
    val senderId: String,
    val content: String,
    val type: String,
    val createAt: Long
)
