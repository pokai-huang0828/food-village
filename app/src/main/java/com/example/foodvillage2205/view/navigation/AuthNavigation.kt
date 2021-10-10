package com.example.foodvillage2205.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodvillage2205.view.screens.SignInScreen

@Composable
fun AuthNavigation(
    SignIn: ()-> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.SignInScreen.route
    ) {
        composable(route = Route.SignInScreen.route) {
            SignInScreen(SignIn = SignIn, navController = navController)
        }
    }
}
