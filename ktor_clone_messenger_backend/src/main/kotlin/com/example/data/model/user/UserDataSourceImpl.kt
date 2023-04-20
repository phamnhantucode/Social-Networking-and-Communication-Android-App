package com.example.data.model.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserDataSourceImpl(
    private val db: CoroutineDatabase
): UserDataSource {
    private val users = db.getCollection<User>()

    override suspend fun getAllUsers(): List<User> {
        return users
            .find()
            .toList()
    }

    override suspend fun getUser(id: String): User? {
        return users.findOne(User::id eq id)
    }

    override suspend fun insertUser(user: User) {
        users.insertOne(user)
    }

    override suspend fun deleteUser(user: User) {
        users.deleteOne(User::id eq user.id)
    }

    override suspend fun findUserByName(username: String): List<User> {

    }


}