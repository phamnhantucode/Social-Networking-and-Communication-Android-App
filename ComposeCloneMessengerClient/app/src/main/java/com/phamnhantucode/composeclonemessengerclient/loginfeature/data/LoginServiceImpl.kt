package com.phamnhantucode.composeclonemessengerclient.loginfeature.data

import com.phamnhantucode.composeclonemessengerclient.core.domain.model.User
import com.phamnhantucode.composeclonemessengerclient.core.util.Resource
import io.ktor.client.*
import io.ktor.client.request.*

class LoginServiceImpl(
    val client: HttpClient
): LoginService {
    override suspend fun login(): User {
        try {
            return client.post<UserDto>(LoginService.Endpoints.Login.url).toUser(client)
        } catch (e: Exception) {
            e.printStackTrace()
            return User.clone()
        }
    }
}