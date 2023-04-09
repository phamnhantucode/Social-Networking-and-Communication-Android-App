package com.example.data.model.object_tranfer_socket

import kotlinx.serialization.Serializable

@Serializable
data class MessageTransfer (
    val chatId: String,
    val senderId: String,
    val text: String,
    val createAt: Long
)
