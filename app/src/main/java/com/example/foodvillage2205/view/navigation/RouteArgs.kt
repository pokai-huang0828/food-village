/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:41:02
 * @ Description: TODO
 */

package com.example.foodvillage2205.view.navigation

import androidx.annotation.StringRes
import com.example.foodvillage2205.R

sealed class RouteArgs {
    companion object {
        val postId: String = "postId"
    }
}