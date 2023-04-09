package com.phamnhantucode.composeclonemessengerclient.core.domain.model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap


data class Image (
    val imageId: String,
    val url: String,
    var data: Bitmap? = null
)