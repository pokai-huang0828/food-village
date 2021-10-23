package com.example.foodvillage2205.view.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.FloatingActionButtonDefaults.elevation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodvillage2205.R
import com.example.foodvillage2205.view.composables.FakeFoodData
import com.example.foodvillage2205.view.composables.FoodListItem
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.*

@ExperimentalFoundationApi
@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(navController) },
        content = {
                  FoodListContent(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Route.DonateScreen.route)
                },
                backgroundColor = SecondaryColor,
                contentColor = Color.White,
                modifier = Modifier
                    .width(115.dp)
                    .height(45.dp)
                    ,
                elevation = elevation(15.dp),
                shape = Shapes.large
                ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "FAB",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = "Donate",
                        fontSize = 18.sp,
                        fontFamily = RobotoSlab,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 2.dp)
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
                    .clickable { }
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
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = White,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }

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
                        contentDescription = "Search",
                        tint = SecondaryColor,
                        modifier = Modifier.size(30.dp)
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

@ExperimentalFoundationApi
@Composable
fun FoodListContent(navController: NavController){
    val foodItems = remember { FakeFoodData.foodList }
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        modifier = Modifier.padding(bottom = 20.dp)
    ){
        items(
            items = foodItems,
            itemContent = {
                   FoodListItem(listItem = it,navController)
            }
        )
    }
}