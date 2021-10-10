package com.example.foodvillage2205.model.repositories

import android.util.Log
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.responses.OnError
import com.example.foodvillage2205.model.responses.OnSuccess
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class PostsRepository {
    private val _collection = FirebaseFirestore.getInstance().collection("posts")
    private val TAG = "Debug"

    fun getPosts() = callbackFlow {
        val snapshotListener = _collection.addSnapshotListener { snapshot, error ->
            val response = if (error == null) {
                val posts = mutableListOf<Post>()

                snapshot?.let { snapshot ->
                    snapshot.documents.mapTo(posts) {
                        Post(
                            name = it.getString("name") ?: "",
                            description = it.getString("description") ?: "",
                            image = it.getString("image") ?: "",
                            id = it.id,
                        )
                    }

                    OnSuccess(posts)
                }
            } else {
                OnError(error)
            }

            offer(response)
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    fun createPost(post: Post) {
        _collection.add(mapPostToData(post))
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun updatePost(post: Post) {
        _collection.document(post.id).set(mapPostToData(post))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    fun deletePost(post: Post) {
        _collection.document(post.id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    private fun mapPostToData(post: Post): HashMap<String, Any> {
        return hashMapOf(
            "description" to post.description,
            "image" to post.image,
            "name" to post.name
        )
    }
}