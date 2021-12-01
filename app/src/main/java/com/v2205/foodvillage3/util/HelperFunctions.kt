/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:03:09
 * @ Description: Contains session storage classes and other possible Helper functions
 */

package com.v2205.foodvillage3.util

import com.v2205.foodvillage3.model.entities.Post


/** SessionPost class allows to store [Post] and makes it available in any file */
object SessionPost {
    var enabled: Boolean = false
    private var post: Post? = null
    fun setSessionPost(value: Post) {
        post = value
    }

    fun getSessionPost(): Post {
        return post!!
    }
}