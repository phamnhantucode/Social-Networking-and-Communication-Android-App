package com.phamnhantucode.composeclonemessengerclient.callfeature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phamnhantucode.composeclonemessengerclient.R
import com.phamnhantucode.composeclonemessengerclient.ui.theme.lightColor2

@Composable
fun GetCallScreen() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Image(
                painter = painterResource(id = R.drawable.user_holder),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                        )
                    )
            ) {
            }
            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "On calling...",
                    style = TextStyle(
                        fontWeight = FontWeight.Light,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = "Username Demo",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 28.sp
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 24.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent)
                        .padding(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_video),
//                            contentDescription = "",
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier
//                                .clip(CircleShape)
//                                .background(lightColor2)
//                                .padding(16.dp)
//                                .size(30.dp)
//                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_call_fill),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Red)
                                .padding(16.dp)
                                .size(30.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_call_fill),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Green)
                                .padding(16.dp)
                                .size(30.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_mic_fill),
//                            contentDescription = "",
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier
//                                .clip(CircleShape)
//                                .background(lightColor2)
//                                .padding(16.dp)
//                                .size(30.dp)
//                        )

//                        Image(
//                            painter = painterResource(id = R.drawable.ic_video),
//                            contentDescription = "",
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier
//                                .clip(CircleShape)
//                                .background(lightColor2)
//                                .padding(16.dp)
//                                .size(30.dp)
//                        )
                    }
                }
            }
        }
    }
}