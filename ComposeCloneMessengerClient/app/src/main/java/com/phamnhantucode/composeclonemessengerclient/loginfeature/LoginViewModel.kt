package com.phamnhantucode.composeclonemessengerclient.loginfeature

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phamnhantucode.composeclonemessengerclient.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val loginRepository: LoginRepository
): ViewModel() {

    var accountTf = mutableStateOf("")
    var passwordTf = mutableStateOf("")
    val authState = MutableStateFlow(AuthState())
    init {

    }

    private fun checkInputMatch() {
//        viewModelScope.launch {
//            .collectLatest {
//                if ()
//            }
//        }
    }

    fun login() {
        viewModelScope.launch {
            loginRepository.login(accountTf.value, password = passwordTf.value).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        authState.emit(AuthState(it.data))
                    }
                    is Resource.Error -> {
                        authState.emit(AuthState(error = it.message.toString()))
                    }
                    else -> {
                        authState.emit(AuthState(loading = true))
                    }
                }
            }
        }
    }

}