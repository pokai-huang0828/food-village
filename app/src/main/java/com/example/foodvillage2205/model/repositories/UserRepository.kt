package com.example.foodvillage2205.model.repositories

import android.util.Log
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.responses.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val _collection = FirebaseFirestore.getInstance().collection("users")
    private val TAG = "Debug"

    @ExperimentalCoroutinesApi
    fun getUsers() = callbackFlow {
        val snapshotListener = _collection.addSnapshotListener { snapshot, error ->
            val response = if (error == null) {
                val users = mutableListOf<User>()

                snapshot?.let { snapshotUsers ->
                    snapshotUsers.documents.mapTo(users) { user ->
                        val snapshotId = user.id
                        user.toObject<User>()!!.apply { id = snapshotId }
                    }
                }
                Resource.Success(users)
            } else Resource.Error("Failed to load posts", error)
            this.trySend(response).isSuccess
        }
        awaitClose { snapshotListener.remove() }
    }

    suspend fun getUserById(id: String): Resource<Any?> {
        val response = _collection
            .document(id)
            .get()
            .await()
        if (response.exists())
            return Resource.Success(response.toObject<User>())
        return Resource.Error("Could not find the user with the given Id.")
    }

    fun createUser(user: User, onResponse: (Resource<*>) -> Unit) {
        _collection.document(user.id).set(user)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot written with ID: ${user.id}")
                onResponse(Resource.Success(user.id))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding user", e)
                onResponse(Resource.Error("Error adding user", e))
            }
    }

    fun updateUser(user: User, onResponse: (Resource<*>) -> Unit) {
        _collection.document(user.id).set(user)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
                onResponse(Resource.Success(user.id))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating document", e)
                onResponse(Resource.Error("Error updating user", e))
            }
    }

    fun deleteUser(user: User, onResponse: (Resource<*>) -> Unit) {
        _collection.document(user.id).delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
                onResponse(Resource.Success(user.id))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
                onResponse(Resource.Error("Error deleting user", e))
            }
    }
}