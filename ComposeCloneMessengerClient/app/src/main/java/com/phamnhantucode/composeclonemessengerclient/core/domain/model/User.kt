package com.phamnhantucode.composeclonemessengerclient.core.domain.model

data class User(
    val userid: String,
    val username: String,
    val password: String,
    val avatar: Image
): java.io.Serializable {
    companion object {
        fun clone(): User {
            return User(
                "",
                username = "",
                "",
                Image("", ""),


            )
        }
    }

}
