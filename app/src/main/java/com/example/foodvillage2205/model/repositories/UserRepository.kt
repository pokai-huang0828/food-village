/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:41:02
 * @ Description: his file contains UserRepository class that makes requests to FirebaseFirestore
 */

package com.example.foodvillage2205.model.repositories

import android.util.Log
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.responses.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * Represents the API class of [User] entity. It allows to make requests to [FirebaseFirestore]
 */
class UserRepository {
    private val _collection = FirebaseFirestore.getInstance().collection("users")
    private val TAG = "Debug"

    /**  Receiving a list of Users that FirebaseFirestore has */
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

    /** Getting a User by ID */
    fun getUserById(userId: String, onResponse: (Resource<*>) -> Unit) {
        _collection.document(userId)
            .get()
            .addOnSuccessListener { user ->
                if (user.toObject<User>() == null) {
                    onResponse(Resource.Error("User doesn't exit", "no user"))
                } else {
                    onResponse(Resource.Success(user.toObject<User>()))
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                onResponse(Resource.Error("User doesn't exit", exception))
            }
    }

    /** Creating a new User */
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

    /** Updating a User */
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

    /** Deleting a User */
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