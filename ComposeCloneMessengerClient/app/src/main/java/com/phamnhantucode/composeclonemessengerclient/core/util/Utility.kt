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
    if (messages[index].senderId == SharedData.user?.userid ?: "") {
        var position = PositionGroupMessage.USER_SINGLE
        if (messages.size ==1) return PositionGroupMessage.USER_SINGLE
        if (index == 0) {
            if (messages[index+1].senderId == messages[index].senderId) {
                return PositionGroupMessage.USER_BOTTOM
            }
            return PositionGroupMessage.USER_SINGLE
        }
        if (index == messages.size-1) {
            if (messages[index-1].senderId == messages[index].senderId) {
                return PositionGroupMessage.USER_TOP
            }
            return PositionGroupMessage.USER_SINGLE
        }
        when {
            messages[index].senderId == messages[index-1].senderId
                    && messages[index].senderId==messages[index+1].senderId -> {
                position = PositionGroupMessage.USER_MIDDLE
            }
            messages[index].senderId == messages[index-1].senderId -> {
                position = PositionGroupMessage.USER_TOP
            }
            messages[index].senderId == messages[index+1].senderId -> {
                position = PositionGroupMessage.USER_BOTTOM
            }
        }
        return position
    } else {
        var position = PositionGroupMessage.ANOTHER_SINGLE
        if (messages.size ==1) return PositionGroupMessage.ANOTHER_SINGLE
        if (index == 0) {
            if (messages[index+1].senderId == messages[index].senderId) {
                return PositionGroupMessage.ANOTHER_BOTTOM
            }
            return PositionGroupMessage.ANOTHER_SINGLE
        }
        if (index == messages.size-1) {
            if (messages[index-1].senderId == messages[index].senderId) {
                return PositionGroupMessage.ANOTHER_TOP
            }
            return PositionGroupMessage.ANOTHER_SINGLE
        }
        when  {
            messages[index].senderId==messages[index-1].senderId && messages[index].senderId==messages[index+1].senderId -> {
                position = PositionGroupMessage.ANOTHER_MIDDLE
            }
            messages[index].senderId==messages[index-1].senderId -> {
                position = PositionGroupMessage.ANOTHER_TOP
            }
            messages[index].senderId==messages[index+1].senderId -> {
                position = PositionGroupMessage.ANOTHER_BOTTOM
            }
        }
        return position
    }
}

