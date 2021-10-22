package com.example.foodvillage2205.view.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodvillage2205.R
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.PrimaryColor
import com.example.foodvillage2205.view.theme.SecondaryColor
import com.example.foodvillage2205.view.theme.ThirdColor
import com.example.foodvillage2205.view.theme.White


@Composable
fun DonateHistory(navController: NavController) {
    Scaffold(
        topBar = { NavBar(navController) },
        content = { ItemList(navController)}
    )
}

@Composable
fun NavBar(
    navController: NavController,
    modifier: Modifier = Modifier) {
        Row(
            modifier = Modifier
                .background(SecondaryColor)
                .fillMaxWidth()
                .height(60.dp)
                .padding(vertical = 3.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {navController.navigate(Route.MainScreen.route)},
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterVertically)) {
                Image(painterResource(R.drawable.food_village_logo_1), "")
            }

            Text(
                text= "Donations",
                color = White,
                fontSize = 30.sp,
                fontFamily = RobotoSlab,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            }

        SearchBox()
}

@Composable
fun SearchBox() {
    var searchText by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.White,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(8.dp)
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                maxLines = 1,
                label = {
                    Text(text = "Search")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "email",
                        tint = SecondaryColor,
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ThirdColor,
                    focusedLabelColor = SecondaryColor,
                    unfocusedBorderColor = SecondaryColor
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                ),
            )
        }
    }
}

@Composable
fun ItemList(navController: NavController) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Item(
            image = painterResource(id = R.drawable.rice_snacks),
            header = "Rice Snacks",
            navController = navController,
            date = "Date: ",
            itemDate = "05-10-2021"
        )
        Item(
            image = painterResource(id = R.drawable.spaghetti),
            header = "Spaghetti",
            navController = navController,
            date = "Date: ",
            itemDate = "05-09-2021"
        )
    }
}

@Composable
fun Item(
    modifier: Modifier = Modifier,
    image: Painter,
    header: String,
    date: String,
    itemDate: String,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = image,
            contentDescription = header,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(160.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        Text(
            text = "$header",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
            color = PrimaryColor,
        )

        Row {
            Text(
                text = date,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = PrimaryColor,
            )
            Text(
                text = itemDate,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
            )
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(backgroundColor = SecondaryColor),
                modifier = Modifier
                    .fillMaxWidth(0.87f)
                    .height(34.dp)
                    .padding(start = 50.dp)

            ) {
                Text(
                    fontFamily = RobotoSlab,
                    color = Color.White,
                    text = "Details",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W900,
                )
            }
        }


    }
}

