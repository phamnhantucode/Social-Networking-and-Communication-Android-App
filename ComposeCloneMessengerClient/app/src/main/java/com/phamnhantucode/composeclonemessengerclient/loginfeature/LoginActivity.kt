package com.phamnhantucode.composeclonemessengerclient.loginfeature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.phamnhantucode.composeclonemessengerclient.ui.theme.ComposeCloneMessengerClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCloneMessengerClientTheme {
                // A surface container using the 'background' color from the theme
                LoginScreen()
            }
        }
    }
}
