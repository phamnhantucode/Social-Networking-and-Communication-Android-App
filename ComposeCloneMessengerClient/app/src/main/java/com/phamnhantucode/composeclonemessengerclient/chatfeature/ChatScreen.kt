package com.phamnhantucode.composeclonemessengerclient.chatfeature

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.phamnhantucode.composeclonemessengerclient.R
import com.phamnhantucode.composeclonemessengerclient.chatfeature.data.MessageDto
import com.phamnhantucode.composeclonemessengerclient.core.SharedData
import com.phamnhantucode.composeclonemessengerclient.core.util.getChatName
import com.phamnhantucode.composeclonemessengerclient.core.util.getPositionGroupMessage
import com.phamnhantucode.composeclonemessengerclient.ui.theme.Blue
import com.phamnhantucode.composeclonemessengerclient.ui.theme.lightColor1
import com.phamnhantucode.composeclonemessengerclient.ui.theme.lightColor2
import com.phamnhantucode.composeclonemessengerclient.ui.theme.lightTextBody

@Composable
fun ChatScreen(
    navController: NavHostController,
    viewModel: ChatViewModel = SharedData.chatViewModel!!
) {
    Log.e("ChatDto", viewModel.sharedVM.currentMessageState.toString())
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarChatScreen(viewModel = viewModel)
            ListMessages(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(Float.MAX_VALUE)
                    .padding(horizontal = 12.dp),
                viewModel = viewModel
            )
            BottomBarChatScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun TopBarChatScreen(
    isDarkTheme: Boolean = false,
    viewModel: ChatViewModel
) {
    var iconColor by remember {
        mutableStateOf(Color.Cyan)
    }
    var backgroundColor by remember {
        mutableStateOf(Color.White)
    }
    if (isDarkTheme) {
        backgroundColor = Color.Black
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                colorFilter = ColorFilter.tint(iconColor),
                modifier = Modifier
                    .size(40.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.user_holder),
                contentDescription = "menu",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
            )
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = getChatName(viewModel.sharedVM.currentMessageState.value),
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    maxLines = 1
                )
                Text(
                    text = "Demo active",
                    style = TextStyle(
                        color = Color.Gray,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    ),
                )
            }

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_call),
                contentDescription = "Call",
                colorFilter = ColorFilter.tint(iconColor),
                modifier = Modifier
                    .size(30.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_video_call),
                contentDescription = "Back",
                colorFilter = ColorFilter.tint(iconColor),
                modifier = Modifier
                    .size(30.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_info),
                contentDescription = "Back",
                colorFilter = ColorFilter.tint(iconColor),
                modifier = Modifier
                    .size(30.dp)
            )
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ListMessages(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel
) {
    val listState = rememberLazyListState()
    var backgroundColor by remember {
        mutableStateOf(lightColor1)
    }
// Remember a CoroutineScope to be able to launch
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        reverseLayout = true
    ) {
        items(viewModel.sharedVM.currentMessageState.value.messages.size) { index ->
            Message(
                position = getPositionGroupMessage(
                    index,
                    viewModel.sharedVM.currentMessageState.value.messages
                ),
                isBelongUser = viewModel.sharedVM.currentMessageState.value.messages[index].senderId == SharedData.user!!.userid,
                messageDto = viewModel.sharedVM.currentMessageState.value.messages[index]
            )
        }
    }

}

@Composable
fun Message(
    isBelongUser: Boolean = true,
    position: PositionGroupMessage = PositionGroupMessage.SINGLE,
    messageDto: MessageDto
) {
    var shape by remember {
        mutableStateOf(
            RoundedCornerShape(
                topStart = 22.dp,
                topEnd = 22.dp,
                bottomStart = 22.dp,
                bottomEnd = 22.dp
            )
        )
    }

    val showState by remember {
        mutableStateOf(false)
    }
    val alignment = if (isBelongUser) Alignment.End else Alignment.Start
    var backgroundColor by remember {
        mutableStateOf(Blue)
    }
    var textColor by remember {
        mutableStateOf(Color.White)
    }


    if (!isBelongUser) {
        backgroundColor = Color.LightGray
        textColor = Color.Black
    }
    when (position) {
        PositionGroupMessage.TOP -> {
            shape = RoundedCornerShape(
                topStart = 22.dp,
                topEnd = 22.dp,
                bottomStart = 22.dp,
                bottomEnd = 10.dp
            )
        }
        PositionGroupMessage.MIDDLE -> {
            shape = RoundedCornerShape(
                topStart = 22.dp,
                topEnd = 10.dp,
                bottomStart = 22.dp,
                bottomEnd = 10.dp
            )
        }
        PositionGroupMessage.BOTTOM -> {
            shape = RoundedCornerShape(
                topStart = 22.dp,
                topEnd = 10.dp,
                bottomStart = 22.dp,
                bottomEnd = 22.dp
            )
        }
        else -> {

        }
    }

    Column(
        horizontalAlignment = alignment,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

            }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Box(
                modifier = Modifier
                    .align(if (isBelongUser) Alignment.CenterEnd else Alignment.CenterStart)
                    .clip(shape)
                    .background(backgroundColor)
                    .padding(10.dp),


                ) {
                Text(
                    text = messageDto.content,
                    style = TextStyle(
                        color = textColor,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ),
                )
            }
        }
    }
}

@Composable
fun BottomBarChatScreen(
    isDarkTheme: Boolean = false,
    viewModel: ChatViewModel = hiltViewModel()
) {
    var chatValue by remember {
        mutableStateOf("")
    }
    var backgroundColor by remember {
        mutableStateOf(Color.White)
    }
    var iconColor by remember {
        mutableStateOf(Color.Cyan)
    }
    if (isDarkTheme) {
        backgroundColor = Color.Black
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 12.dp),

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_camera_fill),
                contentDescription = "Call",
                colorFilter = ColorFilter.tint(iconColor),
                modifier = Modifier
                    .size(24.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_picture),
                contentDescription = "Back",
                colorFilter = ColorFilter.tint(iconColor),
                modifier = Modifier
                    .size(24.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_mic),
                contentDescription = "Back",
                colorFilter = ColorFilter.tint(iconColor),
                modifier = Modifier
                    .size(24.dp)
            )
            BasicTextField(
                value = chatValue,
                onValueChange = {},
                modifier = Modifier
                    .weight(Float.MAX_VALUE)
            ) { innerTextField ->
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(lightColor2)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier,

                        ) {
                        if (chatValue.isNotBlank()) {
                            innerTextField()
                        } else {
                            Text(text = stringResource(id = R.string.sendMessageHolder))
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_smile),
                        contentDescription = "Back",
                        colorFilter = ColorFilter.tint(iconColor),
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.ic_send),
                contentDescription = "Back",
                colorFilter = ColorFilter.tint(iconColor),
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        viewModel.onSendMessage()
                    }
            )
        }
    }
}

