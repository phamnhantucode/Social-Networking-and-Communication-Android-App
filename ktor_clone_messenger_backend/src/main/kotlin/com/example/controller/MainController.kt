package com.example.controller

import com.example.data.model.image.Image
import com.example.data.model.image.ImageDataSource

class MainController(
    val imageDataSource: ImageDataSource
) {

    suspend fun getImage(id: String): Image? {
        return imageDataSource.getImage(id) ?:
            throw ImageNotFound()

    }

}

class ImageNotFound: Exception("Image Not Found")