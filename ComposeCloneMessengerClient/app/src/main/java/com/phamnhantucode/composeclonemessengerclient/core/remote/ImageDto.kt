package com.phamnhantucode.composeclonemessengerclient.core.remote

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.phamnhantucode.composeclonemessengerclient.core.SharedData
import com.phamnhantucode.composeclonemessengerclient.core.domain.model.Image
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@kotlinx.serialization.Serializable
data class ImageDto(
    val id: String,
    val url: String,

    //timestamp
    val createAt: Long? = null,
    val updateAt: Long? = null,
) {
    fun toImage (
    ): Image {
        val image = Image(
            imageId = this.id,
            url = this.url,
        )

        return image
    }
}
