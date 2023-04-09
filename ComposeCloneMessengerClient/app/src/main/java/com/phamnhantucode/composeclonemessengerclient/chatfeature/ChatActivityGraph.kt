package com.phamnhantucode.composeclonemessengerclient.chatfeature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phamnhantucode.composeclonemessengerclient.core.SharedData

@Composable
fun ChatActivityGraph(
    navController: NavHostController = rememberNavController(),
) {
    val viewModel: ChatViewModel = hiltViewModel()
    SharedData.chatViewModel = viewModel
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.startWebSocketService()
            }
            if (event == Lifecycle.Event.ON_DESTROY) {
                viewModel.disconnect()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    NavHost(navController = navController, startDestination = Screens.HomeScreen.route) {
        composable(Screens.HomeScreen.route) {
            HomeScreen(navController = navController, viewModel= viewModel)
        }
        composable(Screens.ChatScreen.route) {
            ChatScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screens.SearchScreen.route) {
            SearchScreen(navController = navController,
                viewModel = viewModel
            )
        }
    }
}