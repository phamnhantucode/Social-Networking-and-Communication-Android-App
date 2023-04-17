package com.phamnhantucode.composeclonemessengerclient.core

import android.content.Context
import com.example.data.model.object_tranfer_socket.Command
import com.phamnhantucode.composeclonemessengerclient.chatfeature.ChatViewModel
import com.phamnhantucode.composeclonemessengerclient.core.domain.model.User
import kotlinx.coroutines.flow.SharedFlow

object SharedData {
    var user: User? = null
    var applicationContext: Context? = null
    var chatViewModel: ChatViewModel? = null
    var sharedFlowSocket: SharedFlow<Command>? = null
}