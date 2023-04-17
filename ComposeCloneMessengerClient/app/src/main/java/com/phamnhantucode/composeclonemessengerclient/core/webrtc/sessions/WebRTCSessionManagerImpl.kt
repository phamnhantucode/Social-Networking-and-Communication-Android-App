package com.phamnhantucode.composeclonemessengerclient.core.webrtc.sessions

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.util.Log
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.content.getSystemService
import com.phamnhantucode.composeclonemessengerclient.chatfeature.ChatViewModel
import com.phamnhantucode.composeclonemessengerclient.core.SharedData
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.SignalingClient
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.WebRTCCallingSessionState
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.WebRTCCommand
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.audio.AudioHandler
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.audio.AudioSwitch
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.audio.AudioSwitchHandler
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.peer.StreamPeerConnection
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.peer.StreamPeerConnectionFactory
import com.phamnhantucode.composeclonemessengerclient.core.webrtc.peer.StreamPeerType
import io.getstream.log.taggedLogger
import io.getstream.webrtc.sample.compose.webrtc.utils.stringify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.webrtc.*
import java.util.*

private const val ICE_SEPARATOR = '$'
val LocalWebRtcSessionManager: ProvidableCompositionLocal<WebRTCSessionManager> =
    staticCompositionLocalOf { error("WebRtcSessionManager was not initialized!") }

class WebRTCSessionManagerImpl(
    private val context: Context,
    override val signalingClient: SignalingClient,
    override val peerConnectionFactory: StreamPeerConnectionFactory,
    val chatId: String,
    val callerId: String
) : WebRTCSessionManager {

    private val logger by taggedLogger("Call:LocalWebRtcSessionManager")
    private val sessionManagerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // used to send local video track to the fragment
    private val _localVideoTrackFlow = MutableSharedFlow<VideoTrack>()
    override val localVideoTrackFlow: SharedFlow<VideoTrack> = _localVideoTrackFlow

    // used to send remote video track to the sender
    private val _remoteVideoTrackFlow = MutableSharedFlow<VideoTrack>()
    override val remoteVideoTrackFlow: SharedFlow<VideoTrack> = _remoteVideoTrackFlow

    // declaring video constraints and setting OfferToReceiveVideo to true
    // this step is mandatory to create valid offer and answer
    private val mediaConstraints = MediaConstraints().apply {
        mandatory.addAll(
            listOf(
                MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"),
                MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true")
            )
        )
    }


    // getting front camera
    private val videoCapturer: VideoCapturer by lazy { buildCameraCapturer() }
    private val cameraManager by lazy { context.getSystemService<CameraManager>() }
    private val cameraEnumerator: Camera2Enumerator by lazy {
        Camera2Enumerator(context)
    }

    private val resolution: CameraEnumerationAndroid.CaptureFormat
        get() {
            val frontCamera = cameraEnumerator.deviceNames.first { cameraName ->
                cameraEnumerator.isFrontFacing(cameraName)
            }
            val supportedFormats = cameraEnumerator.getSupportedFormats(frontCamera) ?: emptyList()
            return supportedFormats.firstOrNull {
                (it.width == 720 || it.width == 480 || it.width == 360)
            } ?: error("There is no matched resolution!")
        }
    private val surfaceTextureHelper = SurfaceTextureHelper.create(
        "SurfaceTextureHelperThread",
        peerConnectionFactory.eglBaseContext
    )

    private val videoSource by lazy {
        peerConnectionFactory.makeVideoSource(videoCapturer.isScreencast).apply {
            videoCapturer.initialize(surfaceTextureHelper, context, this.capturerObserver)
            videoCapturer.startCapture(resolution.width, resolution.height, 30)
        }
    }

    private val localVideoTrack: VideoTrack by lazy {
        peerConnectionFactory.makeVideoTrack(
            source = videoSource,
            trackId = "Video${UUID.randomUUID()}"
        )
    }

    private val audioHandler: AudioHandler by lazy {
        AudioSwitchHandler(context)
    }

    private val audioManager by lazy {
        context.getSystemService<AudioManager>()
    }

    private val audioConstraints: MediaConstraints by lazy {
        buildAudioConstraints()
    }

    private val audioSource by lazy {
        peerConnectionFactory.makeAudioSource(audioConstraints)
    }

    private val localAudioTrack: AudioTrack by lazy {
        peerConnectionFactory.makeAudioTrack(
            source = audioSource,
            trackId = "Audio${UUID.randomUUID()}"
        )
    }

    private var offer: String? = null

    private val peerConnection: StreamPeerConnection by lazy {
        peerConnectionFactory.makePeerConnection(
            coroutineScope = sessionManagerScope,
            configuration = peerConnectionFactory.rtcConfig,
            type = StreamPeerType.SUBSCRIBER,
            mediaConstraints = mediaConstraints,
            onIceCandidateRequest = { iceCandidate, _ ->
                signalingClient.sendCommand(
                    chatId = "chatId",
                    callerId,
                    WebRTCCommand.ICE,
                    "${iceCandidate.sdpMid}$ICE_SEPARATOR${iceCandidate.sdpMLineIndex}$ICE_SEPARATOR${iceCandidate.sdp}"
                )
            },
            onVideoTrack = { rtpTransceiver ->
                val track = rtpTransceiver?.receiver?.track() ?: return@makePeerConnection
                if (track.kind() == MediaStreamTrack.VIDEO_TRACK_KIND) {
                    val videoTrack = track as VideoTrack
                    sessionManagerScope.launch {
                        _remoteVideoTrackFlow.emit(videoTrack)
                    }
                }
            }
        )
    }

    init {
        sessionManagerScope.launch {
            if (SharedData.user!!.userid == callerId) {
                signalingClient.sendCommand(
                    chatId,callerId,
                    WebRTCCommand.STATE,

                    WebRTCCallingSessionState.Impossible.toString()
                )
            }
            signalingClient.signalingCommandFlow
                .collect { commandToValue ->
                    when (commandToValue.first) {
                        WebRTCCommand.OFFER -> handleOffer(commandToValue.second)
                        WebRTCCommand.ANSWER -> handleAnswer(commandToValue.second)
                        WebRTCCommand.ICE -> handleIce(commandToValue.second)
                        else -> Unit
                    }
                }
        }
    }

    override fun sendReadyState() {
        sessionManagerScope.launch {

            signalingClient.sendCommand(
                chatId,
                callerId,
                WebRTCCommand.STATE,
                WebRTCCallingSessionState.Ready.toString()
            )
        }

    }


    override fun onSessionScreenReady() {
        setupAudio()
        peerConnection.connection.addTrack(localVideoTrack)
        peerConnection.connection.addTrack(localAudioTrack)
        sessionManagerScope.launch {
            // sending local video track to show local video from start
            _localVideoTrackFlow.emit(localVideoTrack)

            if (callerId == SharedData.user!!.userid) {
                sendOffer()
            } else {
                if (offer != null) sendAnswer()
            }
        }
    }

    override fun flipCamera() {
        (videoCapturer as? Camera2Capturer)?.switchCamera(null)
    }

    override fun enableMicrophone(enabled: Boolean) {
        audioManager?.isMicrophoneMute = !enabled
    }

    override fun enableCamera(enabled: Boolean) {
        if (enabled) {
            videoCapturer.startCapture(resolution.width, resolution.height, 30)
        } else {
            videoCapturer.stopCapture()
        }
    }

    override fun disconnect() {
        // dispose audio & video tracks.
        remoteVideoTrackFlow.replayCache.forEach { videoTrack ->
            videoTrack.dispose()
        }
        localVideoTrackFlow.replayCache.forEach { videoTrack ->
            videoTrack.dispose()
        }
        localAudioTrack.dispose()
        localVideoTrack.dispose()

        // dispose audio handler and video capturer.
        audioHandler.stop()
        videoCapturer.stopCapture()
        videoCapturer.dispose()

        // dispose signaling clients and socket.
        signalingClient.dispose()
    }

    private fun buildCameraCapturer(): VideoCapturer {
        val manager = cameraManager ?: throw RuntimeException("CameraManager was not initialized!")

        val ids = manager.cameraIdList
        var foundCamera = false
        var cameraId = ""

        for (id in ids) {
            val characteristics = manager.getCameraCharacteristics(id)
            val cameraLensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)

            if (cameraLensFacing == CameraMetadata.LENS_FACING_FRONT) {
                foundCamera = true
                cameraId = id
            }
        }

        if (!foundCamera && ids.isNotEmpty()) {
            cameraId = ids.first()
        }

        val camera2Capturer = Camera2Capturer(context, cameraId, null)
        return camera2Capturer
    }

    private fun buildAudioConstraints(): MediaConstraints {
        val mediaConstraints = MediaConstraints()
        val items = listOf(
            MediaConstraints.KeyValuePair(
                "googEchoCancellation",
                true.toString()
            ),
            MediaConstraints.KeyValuePair(
                "googAutoGainControl",
                true.toString()
            ),
            MediaConstraints.KeyValuePair(
                "googHighpassFilter",
                true.toString()
            ),
            MediaConstraints.KeyValuePair(
                "googNoiseSuppression",
                true.toString()
            ),
            MediaConstraints.KeyValuePair(
                "googTypingNoiseDetection",
                true.toString()
            )
        )

        return mediaConstraints.apply {
            with(optional) {
                add(MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"))
                addAll(items)
            }
        }
    }

    private fun handleOffer(sdp: String) {
        logger.d { "[SDP] handle offer: $sdp" }
        offer = sdp
    }

    private suspend fun handleAnswer(sdp: String) {
        logger.d { "[SDP] handle answer: $sdp" }
        peerConnection.setRemoteDescription(
            SessionDescription(SessionDescription.Type.ANSWER, sdp)
        )
    }

    private suspend fun handleIce(iceMessage: String) {
        Log.e("ICE: ", iceMessage)
        val iceArray = iceMessage.split(ICE_SEPARATOR)
        peerConnection.addIceCandidate(
            IceCandidate(
                iceArray[0],
                iceArray[1].toInt(),
                iceArray[2]
            )
        )
    }

    private fun setupAudio() {
        logger.d { "[setupAudio] #sfu; no args" }
        audioHandler.start()
        audioManager?.mode = AudioManager.MODE_IN_COMMUNICATION

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val devices = audioManager?.availableCommunicationDevices ?: return
            val deviceType = AudioDeviceInfo.TYPE_BUILTIN_SPEAKER

            val device = devices.firstOrNull { it.type == deviceType } ?: return

            val isCommunicationDeviceSet = audioManager?.setCommunicationDevice(device)
            logger.d { "[setupAudio] #sfu; isCommunicationDeviceSet: $isCommunicationDeviceSet" }
        }
    }

    private suspend fun sendAnswer() {
        peerConnection.setRemoteDescription(
            SessionDescription(SessionDescription.Type.OFFER, offer)
        )
        val answer = peerConnection.createAnswer().getOrThrow()
        val result = peerConnection.setLocalDescription(answer)
        result.onSuccess {
            signalingClient.sendCommand("", callerId,WebRTCCommand.ANSWER, answer.description)
        }
        logger.d { "[SDP] send answer: ${answer.stringify()}" }
    }

    private suspend fun sendOffer() {
        val offer = peerConnection.createOffer().getOrThrow()
        val result = peerConnection.setLocalDescription(offer)
        result.onSuccess {
            signalingClient.sendCommand("",callerId, WebRTCCommand.OFFER, offer.description)
        }
        logger.d { "[SDP] send offer: ${offer.stringify()}" }
    }
}