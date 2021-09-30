package com.example.foodvillage

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "google_screen") {
        composable(route = "main_screen") {
            MainScreen(navController = navController)
        }
        composable(route = "welcome_screen") {
            WelcomeScreen(navController = navController)
        }
        composable(route = "google_screen") {
            GoogleScreen(navController = navController)
        }
    }
}