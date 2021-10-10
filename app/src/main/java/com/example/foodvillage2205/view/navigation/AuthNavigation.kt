package com.example.foodvillage2205.view.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodvillage2205.view.screens.SignInScreen
import com.example.foodvillage2205.view.screens.SignUpScreen

@ExperimentalAnimationApi
@Composable
fun AuthNavigation(
    signIn: () -> Unit,
    signUpPass: (email: String, password: String) -> Unit,
    signInPass: (email: String, password: String) -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.SignInScreen.route
    ) {
        composable(route = Route.SignInScreen.route) {
            SignInScreen(navController = navController, signIn = signIn, signInPass = signInPass,)
        }
        composable(route = Route.SignUpScreen.route) {
            SignUpScreen(navController = navController, signUpPass = signUpPass)
        }
    }
}
