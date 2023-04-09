package com.phamnhantucode.composeclonemessengerclient.loginfeature.data

import com.bumptech.glide.Glide
import com.phamnhantucode.composeclonemessengerclient.core.SharedData
import com.phamnhantucode.composeclonemessengerclient.core.domain.model.Image
import com.phamnhantucode.composeclonemessengerclient.core.domain.model.User
import com.phamnhantucode.composeclonemessengerclient.core.remote.ImageDto
import com.phamnhantucode.composeclonemessengerclient.core.remote.WebService
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@kotlinx.serialization.Serializable
data class UserDto(
    val username: String,
    val account: String,
    val password: String,
    val avatarId: String,

    //timestamp
    val createAt: Long? = null,
    val updateAt: Long? = null,

    val id: String,
) {
    suspend fun toUser(client: HttpClient): User {
        return User(
            userid = id,
            username = this.username,
            password = password,
            avatar = client.get<ImageDto>(WebService.Endpoints.Image.getImageUrl(avatarId)).toImage().also {image->
                CoroutineScope(Dispatchers.IO).launch {
                    SharedData.applicationContext?.let {
                        image.data = Glide.with(it)
                            .asBitmap()
                            .load(image.url)
                            .submit()
                            .get()
                    }
                }
            }
//        avatar = Image("")
        )
    }
}
