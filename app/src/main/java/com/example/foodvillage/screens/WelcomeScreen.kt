package com.example.foodvillage

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodvillage.navigation.Screen

@Composable
fun WelcomeScreen(navController: NavController) {
    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
                    .verticalScroll(rememberScrollState())
                    .background(color = Color(red = 241, green = 239, blue = 185))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_wbg),
                        contentDescription = "logo",
                        modifier = Modifier
                            .size(width = 350.dp, height = 200.dp),
                        contentScale = ContentScale.Crop,
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 180.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box {
                        Button(
                            onClick = { navController.navigate(Screen.GoogleScreen.route) },
                            contentPadding = PaddingValues(15.dp),
                            border = BorderStroke(3.dp, Color.Gray),
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = Color.White,
                                contentColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text(
                                "Sign in with Google",
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier
                                    .padding(vertical = 5.dp)
                                    .padding(start = 35.dp, end = 5.dp),
                                fontSize = 17.sp
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.logo_google),
                            contentDescription = "Google logo",
                            modifier = Modifier
                                .size(width = 45.dp, height = 45.dp)
                                .padding(top = 15.dp, start = 10.dp)
                        )
                    }
                }
            }
        },
    )
}