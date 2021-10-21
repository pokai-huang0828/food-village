package com.example.foodvillage2205.model.responses

import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.entities.User
import com.google.firebase.firestore.FirebaseFirestoreException

sealed class PostsResponse
data class PostsResponseSuccess(val posts: List<Post>): PostsResponse()
data class PostsResponseError(val exception: FirebaseFirestoreException?): PostsResponse()

sealed class UsersResponse
data class UsersResponseSuccess(val users: List<User>): UsersResponse()
data class UsersResponseError(val exception: FirebaseFirestoreException?): UsersResponse()

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}