package com.example.data.model.object_tranfer_socket

import kotlinx.serialization.Serializable


@Serializable
data class Command(
    val command: Int,
    val data: String
)
