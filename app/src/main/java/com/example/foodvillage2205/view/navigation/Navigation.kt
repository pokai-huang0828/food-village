package com.example.foodvillage2205.view.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodvillage2205.view.screens.MainScreen
import com.example.foodvillage2205.view.screens.TestScreen

@ExperimentalAnimationApi
@Composable
fun Navigation(signOut: () -> Unit) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.MainScreen.route
    ) {
        composable(route = Route.MainScreen.route) {
            MainScreen(navController = navController, signOut = signOut)
        }
        composable(route = Route.TestScreen.route) {
            TestScreen()
        }
//        composable(route = Screen.GoogleScreen.route) {
//            GoogleScreen(navController = navController)
//        }
    }
}