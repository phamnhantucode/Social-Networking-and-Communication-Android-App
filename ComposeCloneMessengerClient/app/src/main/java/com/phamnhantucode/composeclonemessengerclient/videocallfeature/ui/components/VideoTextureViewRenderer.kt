package com.phamnhantucode.composeclonemessengerclient.videocallfeature.ui.components

import android.content.Context
import android.content.res.Resources
import android.graphics.SurfaceTexture
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import org.webrtc.EglRenderer
import org.webrtc.RendererCommon.RendererEvents
import org.webrtc.ThreadUtils
import org.webrtc.VideoFrame
import org.webrtc.VideoSink
import java.util.concurrent.CountDownLatch

open class VideoTextureViewRenderer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): TextureView(context, attrs), VideoSink, SurfaceTextureListener {

    private val resourceName: String = getResourceName()

    private val eglRenderer: EglRenderer = EglRenderer(resourceName)

    private var rendererEvents: RendererEvents? = null

    private val uiThreadHandler = Handler(Looper.getMainLooper())

    private var isFirstFrameRendered = false

    private var rotatedFrameWidth = 0

    private var rotatedFrameHeight = 0

    private var frameRotation = 0

    init {
        surfaceTextureListener = this
    }

    override fun onFrame(frame: VideoFrame) {
        eglRenderer.onFrame(frame)
        updateFrameData(frame)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        eglRenderer.setLayoutAspectRatio((right - left) / (bottom.toFloat() - top))
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        eglRenderer.createEglSurface(surfaceTexture)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        val completionLatch = CountDownLatch(1)
        eglRenderer.releaseEglSurface { completionLatch.countDown() }
        ThreadUtils.awaitUninterruptibly(completionLatch)
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

    }

    private fun updateFrameData(videoFrame: VideoFrame) {
        if (isFirstFrameRendered) {
            rendererEvents?.onFirstFrameRendered()
            isFirstFrameRendered = true
        }

        if (videoFrame.rotatedWidth != rotatedFrameWidth ||
            videoFrame.rotatedHeight != rotatedFrameHeight ||
            videoFrame.rotation != frameRotation
        ) {
            rotatedFrameWidth = videoFrame.rotatedWidth
            rotatedFrameHeight = videoFrame.rotatedHeight
            frameRotation = videoFrame.rotation

            uiThreadHandler.post {
                rendererEvents?.onFrameResolutionChanged(
                    rotatedFrameWidth,
                    rotatedFrameHeight,
                    frameRotation
                )
            }
        }
    }

    private fun getResourceName(): String {
        return try {
            resources.getResourceEntryName(id)+": "
        } catch (e: Resources.NotFoundException) {
            ""
        }
    }
}