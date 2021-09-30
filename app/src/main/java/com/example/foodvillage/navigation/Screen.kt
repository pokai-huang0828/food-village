package com.example.foodvillage.navigation

import androidx.annotation.StringRes
import com.example.foodvillage.R

sealed class Screen(val route: String, @StringRes val resourceID: Int) {
    object WelcomeScreen : Screen("welcomeScreen", R.string.welcome_screen)
    object GoogleScreen : Screen("googleScreen", R.string.google_screen)
    object MainScreen : Screen("mainScreen", R.string.main_screen)
}