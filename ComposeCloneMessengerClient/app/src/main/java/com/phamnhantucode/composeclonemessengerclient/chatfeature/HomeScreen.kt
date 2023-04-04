package com.phamnhantucode.composeclonemessengerclient.chatfeature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import com.phamnhantucode.composeclonemessengerclient.R
import com.phamnhantucode.composeclonemessengerclient.ui.theme.Blue
import com.phamnhantucode.composeclonemessengerclient.ui.theme.lightColor4
import com.phamnhantucode.composeclonemessengerclient.ui.theme.lightTextBody

@Composable
fun HomeScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(10.dp)
        ) {
            TopBarHome()
            StoryBarHome()
            ListChatHome()
        }

    }
}

@Composable
fun TopBarHome(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.onSurface)
                    .padding(5.dp)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "menu",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(24.dp)
                        .height(24.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                )
            }
            Text(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp

                )
            )
        }

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.onSurface)
                    .padding(5.dp)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = "menu",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(24.dp)
                        .height(24.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                )
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.onSurface)
                    .padding(5.dp)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pen),
                    contentDescription = "menu",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(24.dp)
                        .height(24.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                )
            }
        }

    }
}

@Composable
fun StoryBarHome(
    modifier: Modifier = Modifier,
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .border(
                        shape = CircleShape,
                        width = 2.dp,
                        color = Blue
                    )
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDBE4F1))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "menu",
                    colorFilter = ColorFilter.tint(Blue),
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.Center)
                        .size(20.dp),
                )
            }
        }
        items(10) {
            Box(
                modifier = Modifier
                    .border(
                        shape = CircleShape,
                        width = 2.dp,
                        color = Blue
                    )
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDBE4F1))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user_holder),
                    contentDescription = "menu",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(60.dp)
                        .clip(CircleShape),
                )
            }
        }
    }
}

@Composable
fun ListChatHome() {
    LazyColumn() {
        items(10) {
            SingleChat(
            )
        }
    }
}

@Composable
fun SingleChat(
    isOnline: Boolean = true,
    isTodayStory: Boolean = true,
    isSeenTodayStory: Boolean = true,
    numberOfNonSeenMessage: Int = 0,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        var colorBorder by remember {
            mutableStateOf(Color.Transparent)
        }
        if (isTodayStory) {
            colorBorder = Blue
            if (isSeenTodayStory) {
                colorBorder = Color.Gray
            }
        }

        //Avatar
        Box(
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .border(
                        shape = CircleShape,
                        width = 2.dp,
                        color = colorBorder
                    )
                    .padding(4.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user_holder),
                    contentDescription = "menu",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                )
            }
            if (isOnline) {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .size(16.dp)
                        .border(
                            width = 3.dp,
                            color = MaterialTheme.colors.surface,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .align(Alignment.TopEnd)
                        .background(Color.Green)
                )
            }
        }

        //Chat --
        //Name -
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Username",
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = "Here is a short chat",
                    style = TextStyle(
                        color = lightTextBody,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    ),
                    maxLines = 1
                )

            }
            Column() {
                Text(
                    text = "Timer",
                    style = TextStyle(
                        color = lightTextBody,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    ),
                    maxLines = 1
                )
                if (numberOfNonSeenMessage > 0) {
                    Text(
                        text = numberOfNonSeenMessage.toString(),
                        style = TextStyle(
                            color = lightTextBody,
                            fontWeight = FontWeight.Light,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .clip(CircleShape)
                            .padding(2.dp)
                            .background(lightColor4)
                    )
                } else {
                    Text(
                        text = "",
                        style = TextStyle(
                            color = lightTextBody,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        ),
                    )
                }
            }
        }
    }

}
