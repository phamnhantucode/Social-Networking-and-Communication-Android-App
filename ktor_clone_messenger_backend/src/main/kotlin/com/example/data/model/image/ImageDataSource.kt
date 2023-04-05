package com.example.data.model.image

interface ImageDataSource {
    suspend fun getAllImages(): List<Image>

    suspend fun getImage(id: String): Image?

    suspend fun insertImage(image: Image)

    suspend fun deleteImage(image: Image)
}