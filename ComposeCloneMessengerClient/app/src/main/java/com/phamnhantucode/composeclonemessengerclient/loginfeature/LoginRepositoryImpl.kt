package com.phamnhantucode.composeclonemessengerclient.loginfeature

import com.phamnhantucode.composeclonemessengerclient.core.domain.model.User
import com.phamnhantucode.composeclonemessengerclient.core.util.Resource
import com.phamnhantucode.composeclonemessengerclient.loginfeature.data.LoginService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    val loginService: LoginService
): LoginRepository {
    override suspend fun login(): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading())
            try {
                val data = loginService.login()
                emit(Resource.Success(data))
            } catch (e: Exception) {
                emit(Resource.Error(message = e.message.toString()))
            }
        }
    }

}