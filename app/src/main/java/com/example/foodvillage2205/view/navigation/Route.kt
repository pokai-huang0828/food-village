package com.example.foodvillage2205.view.navigation

import androidx.annotation.StringRes
import com.example.foodvillage2205.R

sealed class Route(val route: String, @StringRes val resourceID: Int) {
    object MainScreen : Route("mainScreen", R.string.main_screen)
    object SignInScreen : Route("signInScreen", R.string.sign_in_screen)
    object SignUpScreen : Route("signUpScreen", R.string.sign_up_screen)
}