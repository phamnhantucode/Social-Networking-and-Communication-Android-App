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
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.SignalingClient
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.WebRTCCallingCommand
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.WebRTCCommand
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.peer.StreamPeerConnectionFactory
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.sessions.WebRTCSessionManager
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.sessions.WebRTCSessionManagerImpl
import com.phamnhantucode.composeclonemessengerclient.loginfeature.LoginActivity
import com.phamnhantucode.composeclonemessengerclient.loginfeature.data.LoginService
import com.phamnhantucode.composeclonemessengerclient.videocallfeature.VideoCallActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
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
                        SharedData.sharedFlowSocket = chatSocketService.observe().shareIn(
                            CoroutineScope(SupervisorJob() + Dispatchers.Default)
                            , SharingStarted.WhileSubscribed(), 1)

                            SharedData.sharedFlowSocket!!.onEach { command ->
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
                                    CommandType.ON_CALL.command -> {
                                        if (!sharedVM.onCalling) {
                                            val webRTCCommand = Json.decodeFromString<WebRTCCallingCommand>(command.data)
                                            Log.e("SIGNALING", webRTCCommand.commandCalling.toString())
                                            context.startActivity(
                                                Intent(
                                                    context,
                                                    VideoCallActivity::class.java
                                                ).apply {
                                                    putExtra("chatId", webRTCCommand.chatId)
                                                    putExtra("callerId", webRTCCommand.callerId)
                                                    setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                }
                                            )
                                            sharedVM.onCalling = true
                                        }
                                    }

                                }
                            }?.launchIn(viewModelScope)
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

    fun onCall(chatId: String) {
        sharedVM.onCalling = true
        context.startActivity(
            Intent(
                context,
                VideoCallActivity::class.java
            ).apply {
                putExtra("chatId",chatId)
                putExtra("callerId", SharedData.user!!.userid)
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
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

    fun sendCommand(command: String) {
        viewModelScope.launch {
            chatSocketService.send(command)
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