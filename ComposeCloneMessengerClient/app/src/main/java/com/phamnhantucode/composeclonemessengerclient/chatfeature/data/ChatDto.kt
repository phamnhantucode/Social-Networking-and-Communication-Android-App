package com.phamnhantucode.composeclonemessengerclient.chatfeature.data

import com.phamnhantucode.composeclonemessengerclient.loginfeature.data.UserDto
import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(

    val name: String? = null,
    val avatarUrl: String?= null,
    val participants: List<UserDto> = emptyList(),
    val messages: List<MessageDto> = emptyList(),

    //timestamp
    val createAt: Long? = null,
    val updateAt: Long? = null,

    val id: String? = null,
)
