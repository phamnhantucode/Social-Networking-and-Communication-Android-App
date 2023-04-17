package com.phamnhantucode.composeclonemessengerclient.videocallfeature

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.phamnhantucode.composeclonemessengerclient.chatfeature.ChatActivity
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.ChatSocketService
import com.phamnhantucode.composeclonemessengerclient.core.SharedData
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.SignalingClient
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.WebRTCCallingSessionState
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.peer.StreamPeerConnectionFactory
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.sessions.LocalWebRtcSessionManager
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.sessions.WebRTCSessionManager
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.sessions.WebRTCSessionManagerImpl
import com.phamnhantucode.composeclonemessengerclient.ui.theme.ComposeCloneMessengerClientTheme
import com.phamnhantucode.composeclonemessengerclient.videocallfeature.ui.screens.CallerScreen
import com.phamnhantucode.composeclonemessengerclient.videocallfeature.ui.screens.GetCallScreen
import com.phamnhantucode.composeclonemessengerclient.videocallfeature.ui.screens.OnCallScreen
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VideoCallActivity : ComponentActivity() {
    @Inject
    lateinit var client: HttpClient

    @Inject
    lateinit var chatSocketService: ChatSocketService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chatId = intent.getStringExtra("chatId")
        val callerId = intent.getStringExtra("callerId")
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 0)
        val callingManager: WebRTCSessionManager = WebRTCSessionManagerImpl(
            context = this,
            signalingClient = SignalingClient(client, chatSocketService),
            peerConnectionFactory = StreamPeerConnectionFactory(this),
            chatId = chatId.toString(),
            callerId = callerId.toString()
        )

        setContent {
            ComposeCloneMessengerClientTheme {
                CompositionLocalProvider(LocalWebRtcSessionManager provides callingManager) {

                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        var onCallScreen by remember {
                            mutableStateOf(false)
                        }
                        val isUserCall by remember {
                            mutableStateOf(callerId == SharedData.user!!.userid)
                        }

                        val state by callingManager.signalingClient.sessionStateFlow.collectAsState()
                        if (state == WebRTCCallingSessionState.Ready && isUserCall) {
                            LaunchedEffect(key1 = Unit) {
//                                callingManager.onSessionScreenReady()
                                onCallScreen = true
                            }
                        }
//                        if (state == WebRTCCallingSessionState.Creating && !isUserCall) {
//                            LaunchedEffect(key1 = Unit) {
//                                coroutineScope {
//                                    launch {
//                                        callingManager.onSessionScreenReady()
//                                    }
//                                }
//                            }
//                        }
//                        if (state == WebRTCCallingSessionState.Active) {
//                            LaunchedEffect(key1 = Unit) {
//                                coroutineScope {
//                                    launch {
//                                        delay(1000)
//                                        onCallScreen = true
//                                    }
//                                }
//                            }
//                        }
                        if (!onCallScreen) {
                            if (isUserCall) {
                                CallerScreen(
                                    state
                                ) {
                                    startActivity(Intent(this, ChatActivity::class.java))
                                    finish()
                                }

                            } else {
                                GetCallScreen(
                                    state,
                                    {
                                        if (state == WebRTCCallingSessionState.Creating) {
                                            onCallScreen = true
                                        }
                                    }
                                ) {
                                    startActivity(Intent(this, ChatActivity::class.java))
                                    finish()
                                }
                            }
                        } else {
                            OnCallScreen()
                        }
                    }
                }
            }
        }
    }
}
