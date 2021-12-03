/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:41:02
 * @ Description: This file represents the Whole Donation history of a User
 * @ If a Post has an Applicant then this Post has a green background
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
fun DonateHistory(navController: NavController, auth: Auth) {

    val scope = rememberCoroutineScope()
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    var userRequest by remember { mutableStateOf("") }

    /** The content consists only of [TopBar] [Drawer] and [FoodListContent] in which we are displaying the food images */
    Scaffold(
        modifier = Modifier.background(Gray),
        topBar = {
            TopBar(
                navController,
                scope = scope,
                text = "Donation History",
                scaffoldState = scaffoldState
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
                screen = "DonateHistory"
            )
        }
    )
}

