package com.example.foodvillage2205.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodvillage2205.R
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.*

@Composable
fun SignInScreen(
    navController: NavController,
    signInWithEmailAndPassword: (email: String, password: String) -> Unit,
    signInWithGoogle: () -> Unit,
    ) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LogoTextBox()


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.padding(top = 125.dp))
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
            onClick = { signInWithEmailAndPassword(email, password) },
            colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryColor),
            modifier = Modifier.padding(ButtonPadding_16dp)
        ) {
            Text(text = "Sign In", color = Color.White)
        }

        Text(text = "I am a new user.",
            style = TextStyle(textDecoration = TextDecoration.Underline),
            modifier = Modifier.clickable { navController.navigate(Route.SignUpScreen.route) }
        )

        OutlinedButton(
            border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 55.dp)
                .padding(top = 15.dp)
                .height(50.dp),
            onClick = {signInWithGoogle() }
        ){
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
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onSurface,
                        text = "Google"
                    )
                    Icon(
                        tint = Color.Transparent,
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = null,
                    )
                }
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
//        val RobotoSlab = FontFamily(
//            Font(R.font.robotoslab_semibold),
//        )
//
//        Text(
//            text = "Food Village",
//            color = Color.White,
//            fontSize = 35.sp,
//            fontFamily = RobotoSlab,
//            textAlign = TextAlign.Center,
//        )
//        Text(
//            text = "Donate & Wasteless",
//            color = Color.White,
//            fontSize = 20.sp,
//            fontFamily = RobotoSlab,
//            textAlign = TextAlign.Center,
//        )
    }
}

