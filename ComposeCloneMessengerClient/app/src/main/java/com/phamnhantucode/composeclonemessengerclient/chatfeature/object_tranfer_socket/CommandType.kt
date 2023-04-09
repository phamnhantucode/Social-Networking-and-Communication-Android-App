package com.example.data.model.object_tranfer_socket

enum class CommandType(val command: Int) {
    
    INIT_CHAT(1),
    SEND_MESSAGE(2),
    RECEIVE_MESSAGE(3)
}