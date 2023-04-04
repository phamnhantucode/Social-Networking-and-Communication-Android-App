package com.phamnhantucode.composeclonemessengerclient.loginfeature.data

import com.phamnhantucode.composeclonemessengerclient.core.domain.model.User
import com.phamnhantucode.composeclonemessengerclient.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface LoginService {

    suspend fun login(): User

    companion object {
        const val BASE_URL = "http://192.168.0.12:8080"
    }

    sealed class Endpoints(val url: String) {
        object Login: Endpoints("$BASE_URL/login")
    }

}