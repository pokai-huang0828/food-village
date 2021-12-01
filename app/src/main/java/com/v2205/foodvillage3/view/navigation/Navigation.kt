/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 11:37:52
 * @ Description: This file contains the Navigation of the Application
 */

package com.v2205.foodvillage3.view.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.v2205.foodvillage3.auth.Auth
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.v2205.foodvillage3.view.navigation.Route
import com.v2205.foodvillage3.view.navigation.RouteArgs
import com.v2205.foodvillage3.view.screens.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
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
            MainScreen(navController = navController, auth = auth)
        }

        composable(route = Route.ProfileScreen.route) {
            ProfileScreen(navController = navController, auth = auth)
        }

        composable(route = Route.DonateScreen.route) {
            DonateScreen(navController = navController, auth = auth)
        }

        composable(route = Route.DonateHistory.route) {
            DonateHistory(navController = navController, auth = auth)
        }

        composable(
            route = Route.DetailScreen.route + "/{${RouteArgs.postId}}",
            arguments = listOf(navArgument(RouteArgs.postId) { type = NavType.StringType })
        ) { backStackEntry ->
            DetailScreen(
                navController = navController,
                postId = backStackEntry.arguments?.getString(RouteArgs.postId),
                auth = auth
            )
        }

        composable(route = Route.SignInScreen.route) {
            SignInScreen(navController = navController, auth = auth)
        }

        composable(route = Route.SignUpScreen.route) {
            SignUpScreen(navController = navController, auth = auth)
        }

        composable(route = Route.ApplyHistory.route) {
            ApplyHistory(navController = navController, auth = auth)
        }
    }
}