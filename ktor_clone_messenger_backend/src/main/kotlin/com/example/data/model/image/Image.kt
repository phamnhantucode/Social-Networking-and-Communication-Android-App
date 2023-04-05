package com.example.data.model.image

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Image(
    @BsonId
    val id: String = ObjectId().toString(),
    val url: String,

    //timestamp
    val createAt: Long? = null,
    val updateAt: Long? = null,
)
