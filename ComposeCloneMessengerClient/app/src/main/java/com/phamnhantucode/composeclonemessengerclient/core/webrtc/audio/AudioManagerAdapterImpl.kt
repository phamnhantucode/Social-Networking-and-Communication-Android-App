package com.phamnhantucode.composeclonemessengerclient.core.webrtc.audio

import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioDeviceInfo
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.util.Log


internal class AudioManagerAdapterImpl(
    private val context: Context,
    private val audioManager: AudioManager,
    private val audioFocusRequest: AudioFocusRequestWrapper = AudioFocusRequestWrapper(),
    private val audioFocusChangeListener: OnAudioFocusChangeListener

): AudioManagerAdapter {
    val TAG = "Call: AudioManager"

    private var saveAudioMode = 0
    private var saveIsMicrophoneMuted = false
    private var savedSpeakerphoneEnable = false
    private var audioRequest: AudioFocusRequest? = null

    init {
        Log.i(TAG, "<init> audioFocusChangeListener: $audioFocusChangeListener" )
    }

    override fun hasEarpiece(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
    }

    override fun hasSpeakerphone(): Boolean {
        return if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
            val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            for (device in devices) {
                if (device.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
                    return true
                }
            }
            false
        } else {
            true
        }
    }

    override fun setAudioFocus() {
        TODO("Not yet implemented")
    }

    override fun enableBluetoothSco(enable: Boolean) {
        TODO("Not yet implemented")
    }

    override fun enableSpeakerphone(enable: Boolean) {
        TODO("Not yet implemented")
    }

    override fun mute(mute: Boolean) {
        TODO("Not yet implemented")
    }

    override fun cacheAudioState() {
        TODO("Not yet implemented")
    }

    override fun restoreAudioState() {
        TODO("Not yet implemented")
    }

}