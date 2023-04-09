package com.phamnhantucode.composeclonemessengerclient.loginfeature.data

import com.phamnhantucode.composeclonemessengerclient.core.domain.model.User
import com.phamnhantucode.composeclonemessengerclient.core.util.Constant.BASE_URL
import com.phamnhantucode.composeclonemessengerclient.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface LoginService {

    suspend fun login(account: String, password: String): User

    companion object {

    }

    sealed class Endpoints(val url: String) {
        object Login: Endpoints("$BASE_URL/login")
    }

}