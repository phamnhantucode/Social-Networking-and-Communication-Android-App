package com.phamnhantucode.composeclonemessengerclient.chatfeature


sealed class Screens(val route: String) {

    object HomeScreen: Screens("home_screen")
    object ChatScreen: Screens("chat_screen")
    object SearchScreen: Screens("search_screen")

}