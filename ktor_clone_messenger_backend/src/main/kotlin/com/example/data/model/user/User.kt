package com.example.data.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class User(
    @BsonId
    val id: String = ObjectId().toString(),
    val username: String,
    val account: String,
    val password: String,
    val avatarId: String,


    //timestamp
    val createAt: Long? = null,
    val updateAt: Long? = null,


)