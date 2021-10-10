package com.example.foodvillage2205.model.responses

import com.example.foodvillage2205.model.entities.Post
import com.google.firebase.firestore.FirebaseFirestoreException

sealed class PostsResponse
data class OnSuccess(val posts: List<Post>): PostsResponse()
data class OnError(val exception: FirebaseFirestoreException?): PostsResponse()