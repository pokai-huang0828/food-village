/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-14 01:28:17
 * @ Description: This file represents Application History Screen. 
 * @ It allows a user to track his/her Posting history
 */

package com.example.foodvillage2205.view.screens

import androidx.compose.foundation.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.foodvillage2205.auth.Auth
import com.example.foodvillage2205.view.composables.Drawer
import com.example.foodvillage2205.view.composables.FoodListContent
import com.example.foodvillage2205.view.composables.TopBar
import com.example.foodvillage2205.view.theme.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@Composable
fun ApplyHistory(navController: NavController, auth: Auth) {

    val scope = rememberCoroutineScope()
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    var userRequest by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.background(Gray),
        topBar = {
            TopBar(
                navController,
                scope = scope,
                scaffoldState = scaffoldState,
                text = "Apply History"
            ) { userSearch ->
                userRequest = userSearch
            }
        },
        scaffoldState = scaffoldState,
        drawerContent = { Drawer(navController = navController, auth = auth) },
        content = {
            FoodListContent(
                navController,
                userRequest = userRequest,
                screen = "ApplyHistory"
            )
        }
    )
}

