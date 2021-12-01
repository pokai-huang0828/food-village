/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 11:37:52
 * @ Description: This is the main page for user to check all kinds of food list to apply
 * or quick donate by clicking right down flow button.
 */

package com.v2205.foodvillage3.view.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.FloatingActionButtonDefaults.elevation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.v2205.foodvillage3.auth.Auth
import com.v2205.foodvillage3.util.SessionPost
import com.v2205.foodvillage3.view.navigation.Route
import com.v2205.foodvillage3.view.composables.Drawer
import com.v2205.foodvillage3.view.composables.FoodListContent
import com.v2205.foodvillage3.view.composables.TopBar
import com.v2205.foodvillage3.view.theme.Gray
import com.v2205.foodvillage3.view.theme.SecondaryColor
import com.v2205.foodvillage3.view.theme.Shapes
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@Composable
fun MainScreen(navController: NavController, auth: Auth) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(
        rememberDrawerState(initialValue = DrawerValue.Closed)
    )
    var userRequest by remember { mutableStateOf("") }
    Scaffold(
        modifier = Modifier.background(Gray),
        topBar = {
            TopBar(
                navController,
                scope = scope,
                scaffoldState = scaffoldState
            ) { userSearch ->
                userRequest = userSearch
            }
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            Drawer(navController = navController, auth = auth)
        },
        content = {
            FoodListContent(navController, userRequest = userRequest,screen = "Home")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    SessionPost.enabled = false
                    navController.navigate(Route.DonateScreen.route)
                },
                backgroundColor = SecondaryColor,
                contentColor = Color.White,
                modifier = Modifier
                    .width(100.dp)
                    .height(45.dp),
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
                        text = "New",
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
