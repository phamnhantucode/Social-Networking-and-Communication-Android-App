package com.phamnhantucode.composeclonemessengerclient.loginfeature.data

import com.phamnhantucode.composeclonemessengerclient.core.domain.model.Image
import com.phamnhantucode.composeclonemessengerclient.core.domain.model.User
import com.phamnhantucode.composeclonemessengerclient.core.remote.ImageDto
import io.ktor.client.*
import io.ktor.client.request.*

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
            username = this.username,
//            avatar = client.get<ImageDto>("").toImage()
        avatar = Image("")
        )
    }
}
