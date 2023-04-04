package com.phamnhantucode.composeclonemessengerclient.core.remote

import com.phamnhantucode.composeclonemessengerclient.core.domain.model.Image

data class ImageDto(
    val url: String
) {
    fun toImage(): Image {
        return Image(
            url = this.url
        )
    }
}
