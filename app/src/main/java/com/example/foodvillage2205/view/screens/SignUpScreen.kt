/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 11:37:52
 * @ Description: This is sign up screen for user to sign up with email and password.
 */

package com.example.foodvillage2205.view.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
import com.example.foodvillage2205.auth.Auth
import com.example.foodvillage2205.view.composables.ErrorMessage
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.*


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
    var errorMessage by remember { mutableStateOf("") }

    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    val (focusRequester1, focusRequester2) = FocusRequester.createRefs()
    val keyboardController = LocalSoftwareKeyboardController.current

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

            ErrorMessage(errorMessage, modifier = Modifier
                .fillMaxWidth(0.7f))

            Button(
                onClick = {
                    if(!password.contentEquals(confirmPassword)){
                        errorMessage = "Passwords do not match"
                        return@Button
                    }

                    auth.signUpWithEmailAndPassword(
                        email,
                        password,
                        onError = {
                            errorMessage = it
                        },
                        onSuccess = {
                            navController.navigate(Route.MainScreen.route)
                        }
                    )
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
        }
    }
}
