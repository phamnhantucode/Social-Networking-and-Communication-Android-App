package com.example.data.model.user

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Friend (
    val id: String =  ObjectId().toString(),
    val senderId: String,
    val receiverId: String,
    val isAccept: Boolean
)