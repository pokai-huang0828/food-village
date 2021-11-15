/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:41:02
 * @ Description: This file contains PostRepository class that makes requests to FirebaseFirestore
 */

package com.example.foodvillage2205.model.repositories

import android.util.Log
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.responses.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * Represents the API class of [Post] entity. It allows to make requests to [FirebaseFirestore]
 */
class PostRepository {
    private val _collection = FirebaseFirestore.getInstance().collection("posts")
    private val TAG = "Debug"

    /**  Receiving a list of Posts that FirebaseFirestore has */
    @ExperimentalCoroutinesApi
    fun getPosts() = callbackFlow {
        val snapshotListener = _collection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                val response = if (error == null) {
                    val posts = mutableListOf<Post>()
                    snapshot?.let { snapshotPosts ->
                        snapshotPosts.documents.mapTo(posts) { post ->
                            val snapshotId = post.id
                            post.toObject<Post>()!!.apply { id = snapshotId }
                        }
                    }
                    Resource.Success(posts)
                } else Resource.Error("Failed to load posts", error)
                this.trySend(response).isSuccess
            }
        awaitClose { snapshotListener.remove() }
    }

    /** Getting a Post by ID */
    fun getPostById(postId: String, onResponse: (Resource<*>) -> Unit) {
        _collection.document(postId)
            .get()
            .addOnSuccessListener { post ->
                onResponse(Resource.Success(post.toObject<Post>()))
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    /** Creating a new Post */
    fun createPost(post: Post, onResponse: (Resource<*>) -> Unit) {
        val newDocRef = _collection.document()
        newDocRef.set(post.apply { id = newDocRef.id })
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written  $documentReference")
                onResponse(Resource.Success(documentReference))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding post", e)
                onResponse(Resource.Error("Error adding post", e))
            }
    }

    /** Updating a Post */
    fun updatePost(post: Post, onResponse: (Resource<*>) -> Unit) {
        _collection.document(post.id)
            .set(post)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
                onResponse(Resource.Success(post.id))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating post", e)
                onResponse(Resource.Error("Error updating post", e))
            }
    }

    /** Deleting a Post */
    fun deletePost(post: Post, onResponse: (Resource<*>) -> Unit) {
        _collection.document(post.id)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
                onResponse(Resource.Success(post.id))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
                onResponse(Resource.Error("Error deleting post", e))
            }
    }

    /** Getting a Post by User ID or technically by any field in Post Collection */
    fun getPostByUser(field: String, id: String, onResponse: (Resource<*>) -> Unit) {
        _collection
            .whereEqualTo(field, id)
            .get()
            .addOnSuccessListener { documents ->
                val listOfResults = mutableListOf<Post>()
                for (document in documents) {
                    listOfResults.add(document.toObject<Post>())
                }
                onResponse(Resource.Success(listOfResults))
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }
}