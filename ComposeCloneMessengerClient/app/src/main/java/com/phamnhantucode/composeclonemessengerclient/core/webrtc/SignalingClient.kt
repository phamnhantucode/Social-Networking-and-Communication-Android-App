package com.phamnhantucode.composeclonemessengerclient.core.webrtc

import android.util.Log
import com.example.data.model.object_tranfer_socket.Command
import com.example.data.model.object_tranfer_socket.CommandType
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatSocketService
import com.phamnhantucode.composeclonemessengerclient.core.SharedData
import io.getstream.log.taggedLogger
import io.ktor.client.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class SignalingClient (
    val client: HttpClient,
    val chatSocketService: ChatSocketService
){

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

    init {
        handleSignaling()
    }

    fun sendCommand(chatId: String, callerId: String, signalingCommand: WebRTCCommand, message: String) {
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
                                callerId = callerId,
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
//
//            }
//        }
//    }

    private fun handleSignaling() {
            SharedData.sharedFlowSocket!!
                .filter {
                    it.command == CommandType.ON_CALL.command
                }
                .map {
                    Json.decodeFromString<WebRTCCallingCommand>(it.data)
                }
                .onEach {
                    when {
                        it.commandCalling.startsWith(WebRTCCommand.STATE.toString(), ignoreCase = true) -> handleStateMessage(it.commandCalling)
                        it.commandCalling.startsWith(WebRTCCommand.OFFER.toString(), ignoreCase = true) -> handleSignalingCommand(WebRTCCommand.OFFER, it.commandCalling)
                        it.commandCalling.startsWith(WebRTCCommand.ANSWER.toString(), ignoreCase = true) -> handleSignalingCommand(WebRTCCommand.ANSWER, it.commandCalling)
                        it.commandCalling.startsWith(WebRTCCommand.ICE.toString(), ignoreCase = true) -> handleSignalingCommand(WebRTCCommand.ICE, it.commandCalling)
                    }
                }
                .launchIn(signalingScope)
    }

    fun handleStateMessage(message: String) {
        val state = getSeparatedMessage(message)

        Log.e("SIGNLING", message)
        _sessionStateFlow.value = WebRTCCallingSessionState.valueOf(state)
    }

    fun handleSignalingCommand(command: WebRTCCommand, text: String) {
        val value = getSeparatedMessage(text)
        Log.e("SIGNLING", text)


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
    val callerId: String,
    val commandCalling: String
)