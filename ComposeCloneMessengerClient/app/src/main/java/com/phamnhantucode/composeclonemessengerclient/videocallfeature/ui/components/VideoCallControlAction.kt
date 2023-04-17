package com.phamnhantucode.composeclonemessengerclient.videocallfeature.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.phamnhantucode.composeclonemessengerclient.R

sealed class CallAction {
    data class ToggleMicroPhone(
        val isEnabled: Boolean
    ) : CallAction()

    data class ToggleCamera(
        val isEnabled: Boolean
    ) : CallAction()

    object FlipCamera : CallAction()

    object LeaveCall : CallAction()
}

data class VideoCallControlAction(
    val icon: Painter,
    val iconTint: Color,
    val background: Color,
    val callAction: CallAction
)

@Composable
fun buildDefaultCallControlActions(
    callMediaState: CallMediaState
): List<VideoCallControlAction> {
    val microphoneIcon =
        painterResource(
            id = if (callMediaState.isMicrophoneEnabled) {
                R.drawable.ic_mic_fill
            } else {
                R.drawable.ic_no_mic_fill
            }
        )

    val cameraIcon = painterResource(
        id = if (callMediaState.isCameraEnabled) {
            R.drawable.ic_video
        } else {
            R.drawable.ic_no_video
        }
    )

    return listOf(
        VideoCallControlAction(
            icon = microphoneIcon,
            iconTint = Color.White,
            background = Color.Black,
            callAction = CallAction.ToggleMicroPhone(callMediaState.isMicrophoneEnabled)
        ),
        VideoCallControlAction(
            icon = cameraIcon,
            iconTint = Color.White,
            background = Color.Black,
            callAction = CallAction.ToggleCamera(callMediaState.isCameraEnabled)
        ),
        VideoCallControlAction(
            icon = painterResource(id = R.drawable.ic_camera_flip),
            iconTint = Color.White,
            background = Color.Black,
            callAction = CallAction.FlipCamera
        ),
        VideoCallControlAction(
            icon = painterResource(id = R.drawable.ic_call_fill),
            iconTint = Color.White,
            background = Color.Red,
            callAction = CallAction.LeaveCall
        )
    )
}
