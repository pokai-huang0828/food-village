package com.example.foodvillage2205.model.repositories

import android.util.Log
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.responses.PostsResponseError
import com.example.foodvillage2205.model.responses.PostsResponseSuccess
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PostRepository {
    private val _collection = FirebaseFirestore.getInstance().collection("posts")
    private val TAG = "Debug"

    fun getPosts() = callbackFlow {
        val snapshotListener = _collection.addSnapshotListener { snapshot, error ->
            val response = if (error == null) {
                val posts = mutableListOf<Post>()

                snapshot?.let { snapshot ->
                    snapshot.documents.mapTo(posts) {
                        mapDataToPost(it)
                    }

                    PostsResponseSuccess(posts)
                }
            } else {
                PostsResponseError(error)
            }

            offer(response)
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    suspend fun getPostById(id: String): Post? {
        var result: Post? = null;

        _collection
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")

                    result = mapDataToPost(document)
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }.await()

        return result;
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

    private fun mapPostToData(post: Post): HashMap<String, Any?> {
        return hashMapOf(
            "title" to post.title,
            "description" to post.description,
            "imageUrl" to post.imageUrl,
            "timestamp" to post.timestamp,
            "userId" to post.userId,
            "email" to post.email,
            "phone" to post.phone,
            "street" to post.street,
            "city" to post.city,
            "province" to post.province,
            "postalCode" to post.postalCode
        )
    }

    private fun mapDataToPost(documentSnapshot: DocumentSnapshot): Post {
        return Post(
            id = documentSnapshot.id,
            title = documentSnapshot.getString("title") ?: "",
            description = documentSnapshot.getString("description") ?: "",
            imageUrl = documentSnapshot.getString("imageUrl") ?: "",
            timestamp = documentSnapshot.getTimestamp("timestamp"),
            userId = documentSnapshot.getString("userId") ?: "",
            email = documentSnapshot.getString("email") ?: "",
            phone = documentSnapshot.getString("phone") ?: "",
            street = documentSnapshot.getString("street") ?: "",
            city = documentSnapshot.getString("city") ?: "",
            province = documentSnapshot.getString("province") ?: "",
            postalCode = documentSnapshot.getString("postalCode") ?: ""
        )
    }
}