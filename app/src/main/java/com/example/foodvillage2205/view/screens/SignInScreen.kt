/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 11:37:52
 * @ Description: TODO
 */

package com.example.foodvillage2205.view.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodvillage2205.auth.Auth
import com.example.foodvillage2205.MainActivity
import com.example.foodvillage2205.R
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi

val RobotoSlab = FontFamily(Font(R.font.robotoslab_semibold))

@ExperimentalFoundationApi
@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SignInScreen(
    navController: NavController,
    auth: Auth,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var displayErroMsg by remember { mutableStateOf(false) }

    val (focusRequester) = FocusRequester.createRefs()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(SecondaryColor)
    ) {
        LogoTextBox()
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(White)
        ) {
            Spacer(modifier = Modifier.padding(top = 2.dp))
            Text(
                text = "Welcome",
                color = PrimaryColor,
                fontSize = 35.sp,
                fontFamily = RobotoSlab,
                textAlign = TextAlign.Center,
            )

            //Email input field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next

                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequester.requestFocus() }
                ),
                label = {
                    Text("Email address")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "email",
                        tint = SecondaryColor,
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ThirdColor,
                    focusedLabelColor = SecondaryColor,
                    unfocusedBorderColor = SecondaryColor
                ),
            )

            //Password input field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                modifier = Modifier.focusRequester(focusRequester),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                label = {
                    Text("Password")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = SecondaryColor,
                    )
                },
                trailingIcon = {
                    val image = if (passwordVisibility)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(imageVector = image, "")
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ThirdColor,
                    focusedLabelColor = SecondaryColor,
                    unfocusedBorderColor = SecondaryColor
                )
            )

            if (displayErroMsg)
                Text(
                    "Login failed, please try again.",
                    color = Danger,
                )

            Button(
                onClick = {
                    auth.signInWithEmailAndPassword(navController, email, password) {
                        // error msg
                        displayErroMsg = true

                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryColor),
                modifier = Modifier
                    .padding(ButtonPadding_16dp)
                    .padding(top = 15.dp)
                    .width(285.dp)
                    .height(50.dp)
                    .shadow(elevation = 5.dp),
            ) {
                Text(
                    fontFamily = RobotoSlab,
                    color = Color.White,
                    text = "Sign In",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W900,
                )
            }

            OutlinedButton(
                border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 55.dp)
                    .padding(top = 10.dp)
                    .height(50.dp)
                    .shadow(elevation = 3.dp),
                onClick = { auth.signInWithGoogle(MainActivity.GOOGLE_AUTH) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        Icon(
                            tint = Color.Unspecified,
                            painter = painterResource(id = R.drawable.googleg_standard_color_18),
                            contentDescription = null,
                        )
                        Text(
                            fontFamily = RobotoSlab,
                            color = Color.Black,
                            text = "Login with Google",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W900,
                        )
                        Icon(
                            tint = Color.Transparent,
                            imageVector = Icons.Default.MailOutline,
                            contentDescription = null,
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.padding(top = 25.dp))

            Text(
                text =
                buildAnnotatedString {
                    append("Don't have an account? Sign up here!")
                    addStyle(
                        style = SpanStyle(
                            color = PrimaryColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textDecoration = TextDecoration.Underline
                        ),
                        start = 23,
                        end = 36
                    )
                },
                modifier = Modifier.clickable { navController.navigate(Route.SignUpScreen.route) }
            )
        }
    }
}

@Composable
fun LogoTextBox() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .height(270.dp)
            .fillMaxWidth()
            .background(SecondaryColor),
    ) {
        Image(
            painter = painterResource(id = R.drawable.food_village_logo_1),
            contentDescription = "logo",
            modifier = Modifier
                .size(180.dp),
        )
        Image(
            painter = painterResource(id = R.drawable.logo_text),
            contentDescription = "logoText",
            modifier = Modifier
                .size(250.dp),
        )
    }
}
