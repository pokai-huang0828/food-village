package com.example.foodvillage.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodvillage.navigation.Screen
import com.example.foodvillage.ui.theme.ButtonPadding_16dp
import com.example.foodvillage.ui.theme.PrimaryColor

@Composable
fun SignInScreen(
    SignIn: (email: String, password: String) -> Unit,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("SIGN IN", style = MaterialTheme.typography.h2)

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
            onClick = { SignIn(email, password) },
            colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryColor),
            modifier = Modifier.padding(ButtonPadding_16dp)
        ) {
            Text(text = "Sign In", color = Color.White)
        }

        Text(text = "I am a new user.",
            style = TextStyle(textDecoration = TextDecoration.Underline),
            modifier = Modifier.clickable { navController.navigate(Screen.SignUpScreen.route) }
        )
    }
}


