package com.phamnhantucode.composeclonemessengerclient.videocallfeature.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
