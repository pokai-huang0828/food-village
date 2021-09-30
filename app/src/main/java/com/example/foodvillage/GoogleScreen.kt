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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun GoogleScreen(navController: NavController) {
    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
                    .verticalScroll(rememberScrollState())
                    .background(color = Color(red = 100, green = 100, blue = 100))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(red = 241, green = 239, blue = 185)),
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
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box {
                        Image(
                            painter = painterResource(id = R.drawable.choose_an_account),
                            contentDescription = "Choose account",
                            modifier = Modifier
                                .size(width = 350.dp, height = 415.dp)
                                .padding(top = 20.dp)
                                .clip(RoundedCornerShape(30.dp))
                        )
                        TextButton(
                            onClick = { navController.navigate("main_screen") },
                            modifier = Modifier
                                .size(width = 350.dp, height = 415.dp)
                                .padding(top = 20.dp)
                                .clip(RoundedCornerShape(30.dp))
                        ) {
                        }
                    }
                }
            }
        },
    )
}