package com.example.data.model.object_tranfer_socket

import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.MessageDto
import kotlinx.serialization.Serializable

@Serializable
data class DataInitChat(
    val participants: List<String>,
    val message: MessageDto? = null,
    val timeSend: Long,
)
