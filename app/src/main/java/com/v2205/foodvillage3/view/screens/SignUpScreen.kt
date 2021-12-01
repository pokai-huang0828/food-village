/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 11:37:52
 * @ Description: This is sign up screen for user to sign up with email and password.
 */

package com.v2205.foodvillage3.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
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
import com.v2205.foodvillage3.auth.Auth
import com.v2205.foodvillage3.view.navigation.Route
import com.v2205.foodvillage3.view.theme.*


@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SignUpScreen(
    navController: NavController,
    auth: Auth
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    val (focusRequester1, focusRequester2) = FocusRequester.createRefs()
    val keyboardController = LocalSoftwareKeyboardController.current

    val openDialog = remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(SecondaryColor)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(White)
        ) {
            LogoTextBox()
            Text(
                text = "SIGN UP",
                color = PrimaryColor,
                fontSize = 35.sp,
                fontFamily = RobotoSlab,
                textAlign = TextAlign.Center,
            )

            //Email input field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text("Email address")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next

                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequester1.requestFocus() }
                ),
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
                )
            )

            //Password input field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                modifier = Modifier.focusRequester(focusRequester1),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequester2.requestFocus() }
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

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                singleLine = true,
                modifier = Modifier.focusRequester(focusRequester2),
                visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                label = {
                    Text("Confirm Password")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Confirm Password",
                        tint = SecondaryColor,
                    )
                },
                trailingIcon = {
                    val image = if (confirmPasswordVisibility)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = {
                        confirmPasswordVisibility = !confirmPasswordVisibility
                    }) {
                        Icon(imageVector = image, "")
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ThirdColor,
                    focusedLabelColor = SecondaryColor,
                    unfocusedBorderColor = SecondaryColor
                ),
            )

            Button(
                onClick = {
                    if( password != confirmPassword ) {
                        openDialog.value = true
                        visible = !visible
                    } else
                    auth.signUpWithEmailAndPassword(navController, email, password) },
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
                    text = "Sign Up",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W900,
                )
            }

            Spacer(modifier = Modifier.padding(top = 25.dp))

            Text(
                text =
                buildAnnotatedString {
                    append("Have an account? Back to Login!")
                    addStyle(
                        style = SpanStyle(
                            color = PrimaryColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textDecoration = TextDecoration.Underline
                        ),
                        start = 17,
                        end = 31
                    )
                },
                modifier = Modifier.clickable { navController.navigate(Route.SignInScreen.route) }
            )

            AnimatedVisibility(visible) {
                Dialog()
            }
        }
    }
}


@Composable
fun Dialog() {
    val openDialog = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(
                        fontFamily = RobotoSlab,
                        text = "Warning ⚠️",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                },
                text = {
                    Text("Your password are not match, please try again.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                    )
                },
                confirmButton = {
                    TextButton(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .fillMaxWidth(0.3f)
                            .background(White),
                        onClick = {
                            openDialog.value = false
                        }) {
                        Text("OK",
                            fontSize = 18.sp,
                            color = PrimaryColor,
                        )
                    }
                },
                backgroundColor = SecondaryColor,
                contentColor = Color.White
            )
        }
    }
}

