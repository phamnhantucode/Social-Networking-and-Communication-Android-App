package com.phamnhantucode.composeclonemessengerclient.core.webrtc.sessions

import com.phamnhantucode.composeclonemessengerclient.chatfeature.ChatViewModel
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.SignalingClient
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.peer.StreamPeerConnectionFactory
import kotlinx.coroutines.flow.SharedFlow
import org.webrtc.VideoTrack

interface WebRTCSessionManager {

    val signalingClient: SignalingClient

    val peerConnectionFactory: StreamPeerConnectionFactory

    val localVideoTrackFlow: SharedFlow<VideoTrack>

    val remoteVideoTrackFlow: SharedFlow<VideoTrack>

    fun onSessionScreenReady()

    fun flipCamera()

    fun enableMicrophone(enabled: Boolean)

    fun enableCamera(enabled: Boolean)

    fun disconnect()
    fun sendReadyState()
}