/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:41:02
 * @ Description: This file represents the Whole Donation history of a User
 * @ If a Post has an Applicant then this Post has a green background
 */

package com.v2205.foodvillage3.view.screens

import androidx.compose.foundation.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.v2205.foodvillage3.auth.Auth
import com.v2205.foodvillage3.view.composables.Drawer
import com.v2205.foodvillage3.view.composables.FoodListContent
import com.v2205.foodvillage3.view.composables.TopBar
import com.v2205.foodvillage3.view.theme.Gray
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@Composable
fun DonateHistory(navController: NavController, auth: Auth) {

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

