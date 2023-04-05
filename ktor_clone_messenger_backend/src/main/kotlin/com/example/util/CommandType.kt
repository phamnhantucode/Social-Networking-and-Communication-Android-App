package com.example.util

enum class CommandType(val command: Int) {
    INIT_CHAT(1),
    SEND_MESSAGE(2),
    RECEIVE_MESSAGE(3)
}