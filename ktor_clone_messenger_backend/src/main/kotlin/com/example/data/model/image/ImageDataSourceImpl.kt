package com.example.data.model.image

import com.mongodb.client.model.DeleteOptions
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ImageDataSourceImpl(
    private val db: CoroutineDatabase
): ImageDataSource {
    private val images = db.getCollection<Image>()

    override suspend fun getAllImages(): List<Image> {
        return images
            .find()
            .toList()
    }

    override suspend fun getImage(id: String): Image? {
        return images.findOne(Image::id eq id)

    }

    override suspend fun insertImage(image: Image) {
        images.insertOne(image)
    }

    override suspend fun deleteImage(image: Image) {
        images.deleteOne(Image::id eq image.id)
    }


}