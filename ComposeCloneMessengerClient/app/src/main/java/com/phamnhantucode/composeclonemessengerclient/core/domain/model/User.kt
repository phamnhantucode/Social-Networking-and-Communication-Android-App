package com.phamnhantucode.composeclonemessengerclient.core.domain.model

data class User(
    val username: String,
    val avatar: Image
) {
    companion object {
        fun clone(): User {
            return User(
                "",
                Image("")
            )
        }
    }

}
