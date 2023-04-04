package com.phamnhantucode.composeclonemessengerclient.loginfeature

import com.phamnhantucode.composeclonemessengerclient.core.domain.model.User
import com.phamnhantucode.composeclonemessengerclient.core.util.Resource
import com.phamnhantucode.composeclonemessengerclient.loginfeature.data.LoginService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface LoginRepository {
    suspend fun login(): Flow<Resource<User>>
}