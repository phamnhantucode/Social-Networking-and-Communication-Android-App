package com.example.data.model.user

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class User(
    val username: String,
    val account: String,
    val password: String,
    val avatarId: String,


    //timestamp
    val createAt: Long? = null,
    val updateAt: Long? = null,

    @BsonId
    val id: String = ObjectId().toString(),
) {

}