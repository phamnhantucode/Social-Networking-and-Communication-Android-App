package com.phamnhantucode.composeclonemessengerclient.videocallfeature.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.phamnhantucode.composeclonemessengerclient.R
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.sessions.LocalWebRtcSessionManager
import com.phamnhantucode.composeclonemessengerclient.videocallfeature.ui.components.*

@Composable
fun OnCallScreen() {
    val sessionManager = LocalWebRtcSessionManager.current
    LaunchedEffect(key1 = Unit) {
        sessionManager.onSessionScreenReady()
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        var parentSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }

        val remoteVideoTrackState by sessionManager.remoteVideoTrackFlow.collectAsState(null)
        val remoteVideoTrack = remoteVideoTrackState

        val localVideoTrackState by sessionManager.localVideoTrackFlow.collectAsState(null)
        val localVideoTrack = localVideoTrackState

        var callMediaState by remember { mutableStateOf(CallMediaState()) }

        if (remoteVideoTrack != null) {
            VideoRenderer(
                videoTrack = remoteVideoTrack,
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { parentSize = it }
            )
        }

        if (localVideoTrack != null && callMediaState.isCameraEnabled) {
            FloatingVideoRenderer(
                modifier = Modifier
                    .size(width = 150.dp, height = 210.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .align(Alignment.TopEnd),
                videoTrack = localVideoTrack,
                parentBounds = parentSize,
                paddingValues = PaddingValues(0.dp)
            )
        }

        val activity = (LocalContext.current as? Activity)

        VideoCallControls(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            callMediaState = callMediaState,
            onCallAction = {
                when (it) {
                    is CallAction.ToggleMicroPhone -> {
                        val enabled = callMediaState.isMicrophoneEnabled.not()
                        callMediaState = callMediaState.copy(isMicrophoneEnabled = enabled)
                        sessionManager.enableMicrophone(enabled)
                    }
                    is CallAction.ToggleCamera -> {
                        val enabled = callMediaState.isCameraEnabled.not()
                        callMediaState = callMediaState.copy(isCameraEnabled = enabled)
                        sessionManager.enableCamera(enabled)
                    }
                    CallAction.FlipCamera -> sessionManager.flipCamera()
                    CallAction.LeaveCall -> {
                        sessionManager.disconnect()
                        activity?.finish()
                    }
                }
            }
        )

    }

}

@Composable
fun VideoCallControls(
    modifier: Modifier,
    callMediaState: CallMediaState,
    actions: List<VideoCallControlAction> = buildDefaultCallControlActions(callMediaState = callMediaState),
    onCallAction: (CallAction) -> Unit
) {
    LazyRow(
        modifier = modifier.padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(actions) { action ->
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(action.background)
            ) {
                Icon(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.Center)
                        .clickable { onCallAction(action.callAction) },
                    tint = action.iconTint,
                    painter = action.icon,
                    contentDescription = null
                )
            }
        }
    }
}

