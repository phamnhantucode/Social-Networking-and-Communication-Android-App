package com.example.data.model.chat

import com.example.data.model.user.User
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
@Serializable
data class Chat(

    val name: String? = null,
    val avatarUrl: String?= null,
    val participants: List<User>,
    val messages: MutableList<Message>,

    //timestamp
    val createAt: Long? = null,
    val updateAt: Long? = null,

    @BsonId
    val id: String = ObjectId().toString()
)
