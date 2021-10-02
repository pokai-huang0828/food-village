package com.example.foodvillage.navigation

import androidx.annotation.StringRes
import com.example.foodvillage.R

sealed class Screen(val route: String, @StringRes val resourceID: Int) {

    object WelcomeScreen : Screen("welcomeScreen", R.string.welcome_screen)
    object GoogleScreen : Screen("googleScreen", R.string.google_screen)
    object MainScreen : Screen("mainScreen", R.string.main_screen)

    object SignInScreen : Screen("signInScreen", R.string.sign_in_screen)
    object SignUpScreen : Screen("signUpScreen", R.string.sign_up_screen)
}