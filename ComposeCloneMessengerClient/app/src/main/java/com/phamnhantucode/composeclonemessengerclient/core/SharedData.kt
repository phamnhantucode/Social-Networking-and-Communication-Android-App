package com.phamnhantucode.composeclonemessengerclient.core

import android.content.Context
import com.phamnhantucode.composeclonemessengerclient.chatfeature.ChatViewModel
import com.phamnhantucode.composeclonemessengerclient.core.domain.model.User

object SharedData {
    var user: User? = null
    var applicationContext: Context? = null
    var chatViewModel: ChatViewModel? = null
}