package com.phamnhantucode.composeclonemessengerclient.loginfeature

import android.annotation.SuppressLint
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phamnhantucode.composeclonemessengerclient.R
import com.phamnhantucode.composeclonemessengerclient.ui.theme.Teal200
import com.phamnhantucode.composeclonemessengerclient.ui.theme.lightTextBody
import kotlin.coroutines.coroutineContext

@Composable
fun LoginScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            val screenHeight by remember {
                mutableStateOf(maxHeight)
            }
            TopBarAuthScreen(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .offset(y = screenHeight * 0.1f)
            )
            AuthSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }
    }
}


@Composable
fun TopBarAuthScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .height(50.dp)
                    .aspectRatio(1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.h2
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AuthSection(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val authState = viewModel.authState.collectAsState()
    if (authState.value.loading) {
        Toast.makeText(LocalContext.current, "Loading", Toast.LENGTH_SHORT)
    }
    if (authState.value.success != null) {
        Toast.makeText(LocalContext.current, "Success", Toast.LENGTH_SHORT)
    }
    Column(
        modifier = modifier
            .padding(horizontal = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val focusRequester = remember { FocusRequester() }
        val localFocusManager =
            LocalFocusManager.current
        Column() {
            Text(
                text = stringResource(id = R.string.account_field),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            )

            OutlinedTextField(
                value = viewModel.accountTf.value,
                maxLines = 1,
                onValueChange = {
                    viewModel.accountTf.value = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusRequester.requestFocus()
                }),
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth(),
                shape = CircleShape,
                placeholder = {
                    Text(text = "abcd.ef@gmail.com")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    leadingIconColor = Color.Black,
                    trailingIconColor = Color.Black,
                    placeholderColor = lightTextBody,
                    unfocusedLabelColor = MaterialTheme.colors.onSecondary
                )
//                isError = !isEmailValid || isEmailOrPasswordEmpty
            )
        }
//        if (!isEmailValid) {
//            Text(
//                text = stringResource(id = R.string.email_error),
//                style = ErrorTextStyle,
//                modifier = Modifier
//                    .fillMaxWidth()
//            )
//        }

        Spacer(modifier = Modifier.height(10.dp))
        //password text field
        Column() {
            Text(
                text = stringResource(id = R.string.passwordField),
                style = TextStyle(
//                    fontFamily = shanTellSansFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            )

            var passwordVisibility by remember {
                mutableStateOf(false)
            }

            val icon = if (passwordVisibility) {
                painterResource(id = R.drawable.ic_baseline_visibility_24)
            } else {
                painterResource(id = R.drawable.ic_baseline_visibility_off_24)
            }
            OutlinedTextField(
                value = viewModel.passwordTf.value,
                maxLines = 1,
                onValueChange = {
                    viewModel.passwordTf.value = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(onDone = {
                    localFocusManager.clearFocus()
                }),
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                shape = CircleShape,
                placeholder = {
                    Text(text = "min 6 characters")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            painter = icon,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    }
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    leadingIconColor = Color.Black,
                    trailingIconColor = Color.Black,
                    placeholderColor = lightTextBody,
                    unfocusedLabelColor = MaterialTheme.colors.onSecondary
                )
//                isError = isEmailOrPasswordEmpty || !isPasswordValid

            )
//            if (!isPasswordValid) {
//                Text(
//                    text = stringResource(id = R.string.password_error),
//                    style = ErrorTextStyle
//                )
//            }
//            if (isEmailOrPasswordEmpty) {
//                Text(
//                    text = stringResource(id = R.string.email_or_password_empty),
//                    style = ErrorTextStyle,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                )
//            }
        }
        Button(
            onClick = viewModel::login,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Teal200
            ),
            shape = CircleShape
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.continue_button_text),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.body1
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}
