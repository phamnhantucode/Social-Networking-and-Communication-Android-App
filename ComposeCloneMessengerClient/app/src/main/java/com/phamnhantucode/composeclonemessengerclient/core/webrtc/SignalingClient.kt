package com.phamnhantucode.composeclonemessengerclient.core.webrtc

import com.example.data.model.object_tranfer_socket.Command
import com.example.data.model.object_tranfer_socket.CommandType
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatSocketService
import io.getstream.log.taggedLogger
import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class SignalingClient{

    @Inject
    private lateinit var client: HttpClient

    @Inject
    private lateinit var chatSocketService: ChatSocketService

    private val logger by taggedLogger("Call:SignalingClient")
    private val signalingScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
//    private val client = OkHttpClient()

    // opening web socket with signaling server

    // session flow to send information about the session state to the subscribers
    private val _sessionStateFlow = MutableStateFlow(WebRTCCallingSessionState.Offline)
    val sessionStateFlow: StateFlow<WebRTCCallingSessionState> = _sessionStateFlow

    // signaling commands to send commands to value pairs to the subscribers
    private val _signalingCommandFlow = MutableSharedFlow<Pair<WebRTCCommand, String>>()
    val signalingCommandFlow: SharedFlow<Pair<WebRTCCommand, String>> = _signalingCommandFlow

    fun sendCommand(chatId: String, signalingCommand: WebRTCCommand, message: String) {
//        logger.d { "[sendCommand] $signalingCommand $message" }
        signalingScope.launch {
            chatSocketService.send(
                Json.encodeToString(
                    Command.serializer(),
                    Command(
                        command = CommandType.ON_CALL.command,
                        data = Json.encodeToString(
                            WebRTCCallingCommand.serializer(),
                            WebRTCCallingCommand(
                                chatId = chatId,
                                commandCalling = "$signalingCommand $message"
                            )
                        )
                    )
                )
            )
        }
    }

//    private inner class SignalingWebSocketListener : WebSocketListener() {
//        override fun onMessage(webSocket: WebSocket, text: String) {
//            when {
//                text.startsWith(WebRTCCommand.STATE.toString(), true) ->
//                    handleStateMessage(text)
//                text.startsWith(WebRTCCommand.OFFER.toString(), true) ->
//                    handleSignalingCommand(WebRTCCommand.OFFER, text)
//                text.startsWith(WebRTCCommand.ANSWER.toString(), true) ->
//                    handleSignalingCommand(WebRTCCommand.ANSWER, text)
//                text.startsWith(WebRTCCommand.ICE.toString(), true) ->
//                    handleSignalingCommand(WebRTCCommand.ICE, text)
//            }
//        }
//    }

    fun handleStateMessage(message: String) {
        val state = getSeparatedMessage(message)
        _sessionStateFlow.value = WebRTCCallingSessionState.valueOf(state)
    }

    fun handleSignalingCommand(command: WebRTCCommand, text: String) {
        val value = getSeparatedMessage(text)
        logger.d { "received signaling: $command $value" }
        signalingScope.launch {
            _signalingCommandFlow.emit(command to value)
        }
    }

    fun getSeparatedMessage(text: String) = text.substringAfter(' ')

    fun dispose() {
        _sessionStateFlow.value = WebRTCCallingSessionState.Offline
        signalingScope.cancel()
//        ws.cancel()
    }
}

enum class WebRTCCallingSessionState {
    Active,
    Creating,
    Ready,
    Impossible,
    Offline
}

enum class WebRTCCommand {
    STATE,
    OFFER,
    ANSWER,
    ICE
}

@Serializable
data class WebRTCCallingCommand(
    val chatId: String,
    val commandCalling: String
)