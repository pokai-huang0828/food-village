/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 17:27:28
 * @ Description: This file contains Resource class
 */
package com.v2205.foodvillage3.model.responses
import com.v2205.foodvillage3.model.repositories.PostRepository
import com.v2205.foodvillage3.model.repositories.UserRepository

/** Class represents responses from Firebase [PostRepository] and [UserRepository] */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

