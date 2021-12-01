/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:41:02
 * @ Description: contains strings for route arguments
 */

package com.v2205.foodvillage3.view.navigation

import androidx.annotation.StringRes
import com.v2205.foodvillage3.R


sealed class RouteArgs {
    companion object {
        val postId: String = "postId"
    }
}