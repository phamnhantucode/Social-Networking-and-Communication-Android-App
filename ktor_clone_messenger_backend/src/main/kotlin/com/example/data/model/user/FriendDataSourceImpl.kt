package com.example.data.model.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq

class FriendDataSourceImpl (
    private var db: CoroutineDatabase,
    private var userDataSource: UserDataSource
): FriendDataSource {
    private val data = db.getCollection<Friend>("friends")
    override suspend fun getAllFriends(userid: String): List<User> {
        return data.find(Friend::senderId eq userid, Friend::receiverId eq userid).toList().map {
            userDataSource.getUser(if (userid == it.senderId) it.receiverId else it.senderId)!!
        }
    }

    override suspend fun getFriends(friendName: String): List<User> {
        userDataSource.
    }

    override suspend fun insertFriend(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFriend(user: User) {
        TODO("Not yet implemented")
    }
}