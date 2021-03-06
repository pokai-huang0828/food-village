/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:03:09
 * @ Description: Contains session storage classes and other possible Helper functions
 */

package com.example.foodvillage2205.util

import com.example.foodvillage2205.model.entities.Post


/** SessionPost class allows to store [Post] and makes it available in any file */
object SessionPost {
    /** [enabled] Boolean for two modes: when it's true = User may Edit Post, when it's False it means that User may create a new Post */
    var enabled: Boolean = false
    private var post: Post? = null
    fun setSessionPost(value: Post) {
        post = value
    }

    fun getSessionPost(): Post {
        return post!!
    }
}