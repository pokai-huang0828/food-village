package com.example.foodvillage

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodvillage.navigation.Screen

@Composable
fun Navigation(signOut: () -> Unit) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController, signOut = signOut)
        }
//        composable(route = Screen.WelcomeScreen.route) {
//            WelcomeScreen(navController = navController)
//        }
//        composable(route = Screen.GoogleScreen.route) {
//            GoogleScreen(navController = navController)
//        }
    }
}