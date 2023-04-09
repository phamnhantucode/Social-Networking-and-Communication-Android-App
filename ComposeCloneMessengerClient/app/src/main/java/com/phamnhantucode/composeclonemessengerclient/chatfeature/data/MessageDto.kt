package com.phamnhantucode.composeclonemessengerclient.chatfeature.data

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(

    val type: String = "text",
    val content: String,
    val senderId: String,

    //timestamp
    val createAt: Long? = null,
    val updateAt: Long? = null,


    val id: String,
)