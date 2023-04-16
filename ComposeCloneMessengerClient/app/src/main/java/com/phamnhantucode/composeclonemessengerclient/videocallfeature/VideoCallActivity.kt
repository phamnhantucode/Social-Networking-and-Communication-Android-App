package com.phamnhantucode.composeclonemessengerclient.videocallfeature

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.phamnhantucode.composeclonemessengerclient.callfeature.CallerScreen
import com.phamnhantucode.composeclonemessengerclient.callfeature.GetCallScreen
import com.phamnhantucode.composeclonemessengerclient.core.SharedData
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.SignalingClient
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.peer.StreamPeerConnectionFactory
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.sessions.LocalWebRtcSessionManager
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.sessions.WebRTCSessionManager
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.sessions.WebRTCSessionManagerImpl
import com.phamnhantucode.composeclonemessengerclient.ui.theme.ComposeCloneMessengerClientTheme


class VideoCallActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chatId = intent.getStringExtra("chatId")
        val callerId = intent.getStringExtra("callerId")
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 0)
        val callingManager: WebRTCSessionManager = WebRTCSessionManagerImpl(
            context = this,
            signalingClient = SignalingClient(),
            peerConnectionFactory = StreamPeerConnectionFactory(this)
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
                        if (!onCallScreen) {
                            if (isUserCall) {
                                CallerScreen(
                                    state = state
                                ) {
                                    onCallScreen = false
                                }

                            } else {
                                GetCallScreen(state = state,
                                    onPickUp = {
                                        onCallScreen = true
                                    },
                                    onHangUp =
                                    {
                                        onCallScreen = false
                                    })
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
