package com.phamnhantucode.composeclonemessengerclient.loginfeature

import com.phamnhantucode.composeclonemessengerclient.core.domain.model.User


data class AuthState(
    val success: User? = null,
    val loading: Boolean = false,
    val error: String = ""
)