package com.example.foodvillage2205.view.navigation

import androidx.annotation.StringRes
import com.example.foodvillage2205.R

sealed class RouteArgs {
    companion object {
        val postId: String = "postId"
    }
}