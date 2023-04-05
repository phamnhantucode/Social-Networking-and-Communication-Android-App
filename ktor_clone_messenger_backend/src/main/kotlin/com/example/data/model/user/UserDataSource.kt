package com.example.data.model.user

interface UserDataSource {
    suspend fun getAllUsers(): List<User>

    suspend fun getUser(id: String): User?

    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User)
}