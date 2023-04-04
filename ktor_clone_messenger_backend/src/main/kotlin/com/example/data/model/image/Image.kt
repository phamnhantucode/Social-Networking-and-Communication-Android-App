package com.example.data.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Image(
    @BsonId
    val id: String = ObjectId().toString(),
    val url: String = ObjectId().toString()
)