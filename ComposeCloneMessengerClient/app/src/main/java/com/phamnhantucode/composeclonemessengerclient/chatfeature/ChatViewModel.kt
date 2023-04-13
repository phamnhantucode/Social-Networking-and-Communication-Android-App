package com.phamnhantucode.composeclonemessengerclient.chatfeature

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.object_tranfer_socket.Command
import com.example.data.model.object_tranfer_socket.CommandType
import com.example.data.model.object_tranfer_socket.MessageTransfer
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatDto
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatService
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatSocketService
import com.phamnhantucode.composeclonemessengerclient.core.SharedData
import com.phamnhantucode.composeclonemessengerclient.core.util.Resource
import com.phamnhantucode.composeclonemessengerclient.loginfeature.LoginActivity
import com.phamnhantucode.composeclonemessengerclient.loginfeature.data.LoginService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val loginService: LoginService,
    val chatService: ChatService,
    val chatSocketService: ChatSocketService,
    @ApplicationContext val context: Context
) : ViewModel() {
    val sharedVM = SaveStateVM


    fun startWebSocketService() {

        SharedData.user?.let { user ->
            getAllChats(userId = user.userid)
            viewModelScope.launch {
                val init = chatSocketService.initWebSocketSession()
                when (init) {
                    is Resource.Success -> {
                        sharedVM.connectedSocket = true
                        chatSocketService.observe()
                            .onEach { command ->
                                when (command.command) {
                                    CommandType.INIT_CHAT.command -> {

                                    }

                                    CommandType.SEND_MESSAGE.command -> {

                                    }

                                    CommandType.RECEIVE_MESSAGE.command -> {
                                        val chat = Json.decodeFromString<ChatDto>(command.data)
                                        sharedVM._chatState.value.chats.find {
                                            it.value?.id == chat.id
                                        }?.let {
                                            it.value = chat
                                            if (sharedVM._currentMessageState.value.id == chat.id) {
                                                sharedVM._currentMessageState.value = chat
                                            }
                                        }
                                    }
                                }
                            }.launchIn(viewModelScope)
                    }
                    is Resource.Error -> {
//                        _toastEvent.emit(result.message ?: "Unknown error")
                        Log.e("ERROR", init.message ?: "Unknown error")
                    }
                    else -> {
                        Log.e("ERROR", init.message ?: "Unknown error")
                    }
                }
            }

        } ?: context.startActivity(Intent(context, LoginActivity::class.java))
    }

    private fun getAllChats(userId: String) {
        viewModelScope.launch {
            val chats = chatService.getAllChats(userId)

            sharedVM._chatState.value = sharedVM.chatState.value.copy(chats = chats.map {
                Log.e("SOS", it.toString())
                mutableStateOf(it)
            })

        }
    }

    fun onSelectChat(state: State<ChatDto>) {
        sharedVM._currentMessageState.value = state.value
        Log.e("ChatViewModel", sharedVM._currentMessageState.value.toString())
    }

    fun onBack() {
        sharedVM._currentMessageState.value = ChatDto()
    }

    fun onSendMessage() {
        if (sharedVM.messageTf.value.isNotBlank()) {
            sharedVM.currentMessageState.value.let { chatDto ->
                sendMessage(
                    chatDto.id!!,
                    SharedData.user!!.userid,
                    sharedVM.messageTf.value,
                    System.currentTimeMillis()
                )
            }
            sharedVM.messageTf.value = ""
        }
    }

    private fun sendMessage(
        chatId: String,
        senderId: String,
        text: String,
        createAt: Long
    ) {
        viewModelScope.launch {
            chatSocketService.send(
                Json.encodeToString(
                    Command.serializer(),
                    Command(
                        CommandType.SEND_MESSAGE.command,
                        data = Json.encodeToString(
                            MessageTransfer.serializer(),
                            MessageTransfer(
                                chatId = chatId,
                                senderId = senderId,
                                text = text,
                                createAt = createAt
                            )
                        )
                    )
                )

            )
        }
    }

    override fun onCleared() {
        super.onCleared()
//        disconnect()
    }

    fun disconnect() {
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }


}