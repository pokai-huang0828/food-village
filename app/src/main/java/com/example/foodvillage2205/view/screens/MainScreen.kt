package com.example.foodvillage2205.view.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodvillage2205.R
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.util.ReadMoreModifier
import com.example.foodvillage2205.view.theme.FourthColor
import com.example.foodvillage2205.view.theme.PrimaryColor
import com.example.foodvillage2205.view.theme.SecondaryColor
import com.example.foodvillage2205.view.theme.ThirdColor

@Composable
fun MainScreen(navController: NavController, signOut: () -> Unit) {
    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                TopBar(signOut = signOut)
                SearchBar()
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
                backgroundColor = SecondaryColor,
                contentColor = Color.White,
                modifier = Modifier.size(79.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "FAB"
                    )
                    Text(
                        text = "Donate",
                        fontSize = 18.sp,
                        fontFamily = RobotoSlab
                    )
                }

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
                .size(79.dp)
                .padding(2.dp)
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
                modifier = Modifier.size(47.dp)
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
}

@Composable
fun SearchBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        elevation = 5.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(8.dp)
        ) {


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

