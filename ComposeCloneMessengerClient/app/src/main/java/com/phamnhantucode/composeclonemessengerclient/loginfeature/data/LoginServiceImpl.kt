package com.phamnhantucode.composeclonemessengerclient.loginfeature.data

import com.phamnhantucode.composeclonemessengerclient.core.domain.model.User
import com.phamnhantucode.composeclonemessengerclient.core.util.Resource
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import javax.inject.Inject

class LoginServiceImpl @Inject constructor(
    val client: HttpClient
): LoginService {
    override suspend fun login(account: String, password: String): User {
        try {
            return client.submitForm<UserDto>(
                formParameters = Parameters.build {
                    append("account", account)
                    append("password", password)
                },
                url = LoginService.Endpoints.Login.url
            ).toUser(client)
        } catch (e: Exception) {
            e.printStackTrace()
            return User.clone()
        }
    }
}