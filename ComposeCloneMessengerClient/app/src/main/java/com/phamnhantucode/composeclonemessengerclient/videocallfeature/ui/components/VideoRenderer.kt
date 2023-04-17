package com.phamnhantucode.composeclonemessengerclient.videocallfeature.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.sessions.LocalWebRtcSessionManager
import org.webrtc.RendererCommon
import org.webrtc.VideoTrack

@Composable
fun VideoRenderer(
    videoTrack: VideoTrack,
    modifier: Modifier
) {
    val trackState: MutableState<VideoTrack?> = remember {
        mutableStateOf(null)
    }
    var view: VideoTextureViewRenderer? by remember {
        mutableStateOf(null)
    }

    DisposableEffect(key1 = videoTrack) {
        onDispose {
            cleanTrack(view, trackState)
        }
    }
    val sessionManager = LocalWebRtcSessionManager.current
    AndroidView(
        factory = { context ->
            VideoTextureViewRenderer(context).apply {
                init(
                    sessionManager.peerConnectionFactory.eglBaseContext,
                    object : RendererCommon.RendererEvents {
                        override fun onFirstFrameRendered() = Unit

                        override fun onFrameResolutionChanged(p0: Int, p1: Int, p2: Int) = Unit
                    }
                )
                setupVideo(trackState, videoTrack, this)
                view = this
            }
        },
        update = { v -> setupVideo(trackState, videoTrack, v) },
        modifier = modifier
    )
}

private fun cleanTrack(
    view: VideoTextureViewRenderer?,
    trackState: MutableState<VideoTrack?>
) {
    view?.let { trackState.value?.removeSink(it) }
    trackState.value = null
}

private fun setupVideo(
    trackState: MutableState<VideoTrack?>,
    track: VideoTrack,
    renderer: VideoTextureViewRenderer
) {
    if (trackState.value == track) {
        return
    }

    cleanTrack(renderer, trackState)

    trackState.value = track
    track.addSink(renderer)
}
