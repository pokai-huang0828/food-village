package com.example.foodvillage2205.view.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foodvillage2205.Auth
import com.example.foodvillage2205.view.screens.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Navigation(auth: Auth) {
    val navController = rememberNavController()
    val startDestination by remember {
        mutableStateOf(
            when (auth.currentUser) {
                null -> Route.SplashScreen.route
                else -> Route.SplashScreen.route
            }
        )
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(route = Route.SplashScreen.route) {
            SplashScreen(navController = navController, auth = auth)
        }

        composable(route = Route.MainScreen.route) {
            MainScreen(navController = navController)
        }

        composable(route = Route.ProfileScreen.route) {
            ProfileScreen(navController = navController, auth = auth)
        }

        composable(route = Route.DonateScreen.route) {
            DonateScreen(navController = navController, auth = auth)
        }

        composable(route = Route.DonateHistory.route) {
            DonateHistory(navController = navController)
        }

        composable(
            route = Route.DetailScreen.route + "/{${RouteArgs.postId}}",
            arguments = listOf(navArgument(RouteArgs.postId) { type = NavType.StringType })
        ) { backStackEntry ->
            DetailScreen(
                navController = navController,
                postId = backStackEntry.arguments?.getString(RouteArgs.postId)
            )
        }

        composable(route = Route.SignInScreen.route) {
            SignInScreen(navController = navController, auth = auth)
        }

        composable(route = Route.SignUpScreen.route) {
            SignUpScreen(navController = navController, auth = auth)
        }
    }
}