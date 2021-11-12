package com.example.foodvillage2205.view.navigation

import androidx.annotation.StringRes
import com.example.foodvillage2205.R

sealed class Route(val route: String, @StringRes val resourceID: Int) {
    object SplashScreen : Route("splashScreen", R.string.splash_screen)
    object MainScreen : Route("mainScreen", R.string.main_screen)
    object ProfileScreen : Route("profileScreen", R.string.profile_screen)
    object DonateScreen : Route("donateScreen", R.string.donate_screen)
    object DonateHistory : Route("donateHistory", R.string.donate_history)
    object SignInScreen : Route("signInScreen", R.string.sign_in_screen)
    object SignUpScreen : Route("signUpScreen", R.string.sign_up_screen)
    object DetailScreen : Route("detailScreen", R.string.detail_screen)
    object ApplyHistory : Route("applyHistory", R.string.apply_history)
}