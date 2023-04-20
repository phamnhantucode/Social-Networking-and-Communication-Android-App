package com.example.data.model.user

interface FriendDataSource {
    suspend fun getAllFriends(userid: String): List<User>

    suspend fun getFriends(friendName: String): List<User>

    suspend fun insertFriend(user: User)

    suspend fun deleteFriend(user: User)
}