package com.example.foodvillage2205.view.navigation

import androidx.annotation.StringRes
import com.example.foodvillage2205.R

sealed class Route(val route: String, @StringRes val resourceID: Int) {

    object WelcomeScreen : Route("welcomeScreen", R.string.welcome_screen)
    object GoogleScreen : Route("googleScreen", R.string.google_screen)
    object MainScreen : Route("mainScreen", R.string.main_screen)
    object TestScreen : Route("testScreen", R.string.test_screen)
    object ProfileScreen : Route("profileScreen", R.string.profile_screen)
    object DonateHistory : Route("donateHistory", R.string.donate_history)

    object SignInScreen : Route("signInScreen", R.string.sign_in_screen)
    object SignUpScreen : Route("signUpScreen", R.string.sign_up_screen)
}