package com.example.foodvillage2205.view.navigation

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodvillage2205.Auth
import com.example.foodvillage2205.view.screens.*

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Navigation(auth: Auth) {
    val navController = rememberNavController()
    val startDestination by remember {
        mutableStateOf(
            when (auth.currentUser) {
                null -> Route.SignInScreen.route
                else -> Route.MainScreen.route
            }
        )
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Route.MainScreen.route) {
            MainScreen(navController = navController)
        }

        composable(route = Route.ProfileScreen.route) {
            ProfileScreen(navController = navController, auth = auth)
        }

        composable(route = Route.DonateScreen.route) {
            DonateScreen(navController = navController)
        }

        composable(route = Route.DonateHistory.route) {
            DonateHistory(navController = navController)
        }

        composable(route = Route.DetailScreen.route) {
            DetailScreen(navController = navController)
        }

        composable(route = Route.SignInScreen.route) {
            SignInScreen(navController = navController, auth = auth)
        }

        composable(route = Route.SignUpScreen.route) {
            SignUpScreen(navController = navController, auth = auth)
        }
    }
}