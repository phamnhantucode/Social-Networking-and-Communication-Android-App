package com.phamnhantucode.composeclonemessengerclient.core.util

import com.phamnhantucode.composeclonemessengerclient.chatfeature.PositionGroupMessage
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatDto
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.MessageDto
import com.phamnhantucode.composeclonemessengerclient.core.SharedData
import java.text.SimpleDateFormat
import java.util.*

fun toDateTimeString(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("HH:mm")
    return format.format(date)
}

fun getChatName(chatDto: ChatDto): String {
    var chatName = chatDto.name ?: if (chatDto.participants.size == 2)
        if (chatDto.participants[0].id == SharedData.user?.userid)
            chatDto.participants[1].username
        else chatDto.participants[0].username
    else chatDto.participants.map {
        it.username.split(" ").get(0)
    }.joinToString(", ")


    return chatName
}

fun getTimeMessage(chatDto: ChatDto): String {
    return if (chatDto.messages.size == 0) "" else chatDto.messages.get(
        0
    ).createAt?.let {
        toDateTimeString(
            it
        )
    } ?: ""
}

fun getPositionGroupMessage(index: Int, messages: List<MessageDto>): PositionGroupMessage {
    var position = PositionGroupMessage.SINGLE
    if (messages.size ==1) return PositionGroupMessage.SINGLE
    if (index == 0) {
        if (messages[index+1].senderId == messages[index].senderId) {
            return PositionGroupMessage.BOTTOM
        }
        return PositionGroupMessage.SINGLE
    }
    if (index == messages.size-1) {
        if (messages[index-1].senderId == messages[index].senderId) {
            return PositionGroupMessage.TOP
        }
        return PositionGroupMessage.SINGLE
    }
    when (messages[index].senderId) {
        messages[index-1].senderId, messages[index+1].senderId -> {
            position = PositionGroupMessage.MIDDLE
        }
        messages[index-1].senderId -> {
            position = PositionGroupMessage.TOP
        }
        messages[index+1].senderId -> {
            position = PositionGroupMessage.BOTTOM
        }
    }
    return position
}

