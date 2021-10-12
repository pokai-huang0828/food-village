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