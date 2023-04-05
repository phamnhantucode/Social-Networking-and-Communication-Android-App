package com.example.data.model.object_tranfer_socket

import com.example.data.model.chat.Message
import kotlinx.serialization.Serializable

@Serializable
data class DataInitChat(
    val participants: List<String>,
    val message: Message? = null,
    val timeSend: Long,
)
