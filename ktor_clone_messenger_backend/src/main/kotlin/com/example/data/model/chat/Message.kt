package com.example.data.model.chat

import com.example.data.model.user.User
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Message(

    val type: String = MessageType.TEXT.toString(),
    val content: String,
    val senderId: String,

    //timestamp
    val createAt: Long? = null,
    val updateAt: Long? = null,


    @BsonId
    val id: String = ObjectId().toString()
)