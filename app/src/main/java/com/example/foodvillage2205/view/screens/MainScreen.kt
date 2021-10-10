package com.example.foodvillage2205.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodvillage2205.R
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.util.ReadMoreModifier

@Composable
fun MainScreen(navController: NavController, signOut: () -> Unit) {
    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TopBar(signOut = signOut)
                SearchRow()
                Spacer(modifier = Modifier.heightIn(20.dp))
                FoodItem(
                    image = painterResource(id = R.drawable.rice_snacks),
                    header = "Rice Snacks",
                    navController = navController,
                    itemDescription = "Our Rice Crisps come in a delicious variety of flavours to satisfy your snack cravings any \ntime of day."
                )
                Spacer(modifier = Modifier.heightIn(20.dp))
                FoodItem(
                    image = painterResource(id = R.drawable.spaghetti),
                    header = "Spaghetti",
                    navController = navController,
                    itemDescription = "It is the quintessential Italian pasta. It is long \n- like a string " +
                            "- round in cross-section and made from durum wheat semolina."
                )
                Spacer(modifier = Modifier.heightIn(100.dp))
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                },
                backgroundColor = Color.Gray,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "FAB"
                )
            }
        }

    )
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    signOut: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Account",
                tint = Color.DarkGray,
                modifier = Modifier.size(47.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = "Messages",
                tint = Color.DarkGray,
                modifier = Modifier.size(47.dp).clickable { signOut() }
            )
        }
    }
}

@Composable
fun SearchRow(
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Account",
                tint = Color.Green,
                modifier = Modifier
                    .size(47.dp)
                    .padding(top = 7.dp, start = 5.dp)
            )
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
            )
        }
    }
}

@Composable
fun FoodItem(
    modifier: Modifier = Modifier,
    image: Painter,
    header: String,
    itemDescription: String,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = image,
            contentDescription = header,
            modifier = Modifier
                .size(width = 250.dp, height = 150.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop,

            )
        Text(
            text = "$header:",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
        )
        Text(
            text = itemDescription,
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
            fontStyle = FontStyle.Italic
        )
        Box {
            Image(
                painter = painterResource(id = R.drawable.read_more_btn),
                contentDescription = "Read more btn",
                modifier = ReadMoreModifier(),
                contentScale = ContentScale.Crop
            )
            TextButton(
                onClick = {  },
                modifier = ReadMoreModifier()
            ) {
            }
        }
    }
}

