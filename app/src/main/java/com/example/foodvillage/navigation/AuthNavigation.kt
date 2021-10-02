package com.example.foodvillage.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodvillage.screens.SignInScreen
import com.example.foodvillage.screens.SignUpScreen

@Composable
fun AuthNavigation(
    SignUp: (email:String, password: String)-> Unit,
    SignIn: (email:String, password: String)-> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SignInScreen.route
    ) {
        composable(route = Screen.SignInScreen.route) {
            SignInScreen(SignIn = SignIn, navController = navController)
        }
        composable(route = Screen.SignUpScreen.route) {
            SignUpScreen(SignUp = SignUp, navController = navController)
        }
    }
}
