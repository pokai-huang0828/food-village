package com.example.foodvillage2205.view.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodvillage2205.R
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.*

@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(navController) },
        content = {
            FoodList(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Route.DonateScreen.route)
                },
                backgroundColor = SecondaryColor,
                contentColor = Color.White,
                modifier = Modifier.size(85.dp),

                ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "FAB",
                        modifier = Modifier.size(39.dp)
                    )
                    Text(
                        text = "Donate",
                        fontSize = 18.sp,
                        fontFamily = RobotoSlab,
                    )
                }
            }
        }
    )
}

@Composable
fun TopBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Column() {
        Row(
            modifier = Modifier
                .background(SecondaryColor)
                .fillMaxWidth()
                .height(60.dp)
                .padding(vertical = 3.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Food Village",
                color = White,
                fontFamily = RobotoSlab,
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(5.dp)
                    .padding(start = 5.dp)

            )

            Row(
                modifier = Modifier
                    .background(SecondaryColor)
                    .height(60.dp)
                    .padding(2.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigate(Route.ProfileScreen.route) },
                    modifier = Modifier
                        .padding(5.dp)
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(PrimaryColor)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Account",
                        tint = White,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        }
        SearchBar()
    }
}

@Composable
fun SearchBar() {
    var searchText by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            ,
        color = Gray,
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
fun FoodList(navController: NavController) {
    Column(
        modifier = Modifier
            .background(Gray)
            .verticalScroll(rememberScrollState())
    ) {
        FoodItem(
            image = painterResource(id = R.drawable.rice_snacks),
            header = "Rice Snacks",
            navController = navController,
            itemDescription = "Our Rice Crisps come in a delicious variety of flavours to satisfy your snack cravings any \ntime of day."
        )
        FoodItem(
            image = painterResource(id = R.drawable.spaghetti),
            header = "Spaghetti",
            navController = navController,
            itemDescription = "It is the quintessential Italian pasta. It is long \n- like a string " +
                    "- round in cross-section and made from durum wheat semolina."
        )

        Spacer(modifier = Modifier.padding(bottom = 80.dp))
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
    Card(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .padding(10.dp),
        shape = Shapes.medium,
        backgroundColor = White,
        elevation = 2.dp,

    ) {
        Column() {
            Image(
                painter = image,
                contentDescription = header,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
            Text(
                text = "$header:",
                fontWeight = FontWeight.Bold,
                fontFamily = RobotoSlab,
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                color = PrimaryColor,

            )
            Text(
                text = itemDescription,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
            )

//        Button(
//            onClick = { navController.navigate(Route.DetailScreen.route) },
//            colors = ButtonDefaults.buttonColors(backgroundColor = SecondaryColor),
//            modifier = Modifier
//                .padding(top = 15.dp)
//                .width(120.dp)
//                .height(25.dp),
//        ) {
//            Text(
//                fontFamily = RobotoSlab,
//                color = Color.White,
//                text = "Read More",
//                fontSize = 14.sp,
//                fontWeight = FontWeight.W900,
//            )
//        }
        }

    }
}
