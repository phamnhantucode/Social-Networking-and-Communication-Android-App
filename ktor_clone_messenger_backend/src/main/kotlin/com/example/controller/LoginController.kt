package com.example.controller

import com.example.data.model.user.User
import com.example.data.model.user.UserDataSource
import com.example.session.MessengerSession


class LoginController(
    private val userDataSource: UserDataSource
) {
    suspend fun login(account: String, password: String): User {
        userDataSource.getAllUsers().find { user ->
            user.account == account
        }?.let {
            if (it.password == password) {
                return it
            } else throw PasswordNotMatchException()
        } ?: throw AccountNotMatchException()
    }

    suspend fun signup(account: String, password: String): User =
        userDataSource.getAllUsers().find { user ->
            user.account == account
        }?.let {
            throw AccountExitsException()
        } ?: User(
            username = account,
            password = password,
            account = account,
            avatarId = "image_default",
            createAt = System.currentTimeMillis(),
            updateAt = System.currentTimeMillis()
        ).let {
            userDataSource.insertUser(user = it)
            it
        }
}
class AccountNotMatchException: Exception("Account Not Match")
class PasswordNotMatchException: Exception("Password Not Match")
class AccountExitsException: Exception("Account exits")