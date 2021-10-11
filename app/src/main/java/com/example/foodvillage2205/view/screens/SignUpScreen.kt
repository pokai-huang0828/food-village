package com.example.foodvillage2205.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.ButtonPadding_16dp
import com.example.foodvillage2205.view.theme.PrimaryColor


@Composable
fun SignUpScreen(
    navController: NavController,
    signUpWithEmailAndPassword: (email: String, password: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("SIGN UP", style = MaterialTheme.typography.h2)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text("Password")
            }
        )

        Button(
            onClick = { signUpWithEmailAndPassword(email, password) },
            colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryColor),
            modifier = Modifier.padding(ButtonPadding_16dp)
        ) {
            Text(text = "Sign Up", color = Color.White)
        }

        Text(text = "I have an account.",
            style = TextStyle(textDecoration = TextDecoration.Underline),
            modifier = Modifier.clickable { navController.navigate(Route.SignInScreen.route) }
        )
    }
}



