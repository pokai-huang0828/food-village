package com.example.foodvillage2205.view.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.example.foodvillage2205.view.theme.PrimaryColor
import com.example.foodvillage2205.view.theme.SecondaryColor
import com.example.foodvillage2205.view.theme.ThirdColor

@Composable
fun MainScreen(navController: NavController, signOut: () -> Unit) {
    Scaffold(
        topBar = { TopBar(navController, signOut = signOut) },
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
    signOut: () -> Unit
) {
    Column() {
        Row(
            modifier = Modifier
                .background(SecondaryColor)
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.food_village_logo_1),
                contentDescription = "logo",
                alignment = Alignment.CenterStart,
                modifier = Modifier
                    .size(80.dp)
                    .padding(3.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.logo_text),
                contentDescription = "logo",
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(190.dp)
                    .padding(2.dp)
            )

            Row(
                modifier = Modifier
                    .background(SecondaryColor)
                    .height(60.dp)
                    .padding(2.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Account",
                    tint = ThirdColor,
                    modifier = Modifier
                        .size(47.dp)
                        .clickable {navController.navigate(Route.ProfileScreen.route) }
                )

                Spacer(modifier = modifier.padding(5.dp))

                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "Messages",
                    tint = ThirdColor,
                    modifier = Modifier
                        .size(47.dp)
                        .clickable { signOut() }
                )
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
fun FoodList(navController: NavController) {
    Column(
        modifier = Modifier
            .padding(10.dp)
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
            text = "$header:",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
            color = PrimaryColor,
        )
        Text(
            text = itemDescription,
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
        )

        Button(
            onClick = {  },
            colors = ButtonDefaults.buttonColors(backgroundColor = SecondaryColor),
            modifier = Modifier
                .padding(top = 15.dp)
                .width(120.dp)
                .height(35.dp),
        ) {
            Text(
                fontFamily = RobotoSlab,
                color = Color.White,
                text = "Read More",
                fontSize = 14.sp,
                fontWeight = FontWeight.W900,
            )
        }
    }
}
