package com.phamnhantucode.composeclonemessengerclient

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.phamnhantucode.composeclonemessengerclient.callfeature.GetCallScreen
import com.phamnhantucode.composeclonemessengerclient.callfeature.OnCallScreen
import com.phamnhantucode.composeclonemessengerclient.chatfeature.ChatScreen
import com.phamnhantucode.composeclonemessengerclient.chatfeature.HomeScreen
import com.phamnhantucode.composeclonemessengerclient.loginfeature.LoginActivity
import com.phamnhantucode.composeclonemessengerclient.ui.theme.ComposeCloneMessengerClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContent {
//            ComposeCloneMessengerClientTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    Greeting("Android")
//                }
//            }
//        }
        startActivity(Intent(this, LoginActivity::class.java))
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

//@Preview(showBackground = true, device = Devices.PIXEL_4)
//@Composable
//fun DefaultPreview() {
//    ComposeCloneMessengerClientTheme {
//        HomeScreen()
//    }
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4)
//@Composable
//fun DarkTheme() {
//    ComposeCloneMessengerClientTheme(darkTheme = true) {
//        ChatScreen()
//    }
//}