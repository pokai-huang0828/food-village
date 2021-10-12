package com.example.foodvillage2205.model.repositories

import android.util.Log
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.responses.UsersResponseError
import com.example.foodvillage2205.model.responses.UsersResponseSuccess
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val _collection = FirebaseFirestore.getInstance().collection("users")
    private val TAG = "Debug"

    fun getUsers() = callbackFlow {
        val snapshotListener = _collection.addSnapshotListener { snapshot, error ->
            val response = if (error == null) {
                val users = mutableListOf<User>()

                snapshot?.let { snapshot ->
                    snapshot.documents.mapTo(users) {
                        mapDataToUser(it)
                    }

                    UsersResponseSuccess(users)
                }
            } else {
                UsersResponseError(error)
            }

            offer(response)
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    suspend fun getUserById(id: String): User? {
        var result: User? = null;

        _collection
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")

                    result = mapDataToUser(document)
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }.await()

        return result
    }

    fun createUser(user: User) : String? {
        var userId : String? = null

        _collection.add(mapUserToData(user))
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")

                userId = documentReference.id
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

        return userId;
    }

    fun updateUser(user: User) {
        _collection.document(user.id).set(mapUserToData(user))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    fun deleteUser(user: User) {
        _collection.document(user.id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    private fun mapUserToData(user: User): HashMap<String, Any?> {
        return hashMapOf(
            "id" to user.id,
            "name" to user.name,
            "email" to user.email,
            "thumbnailUrl" to user.thumbnailUrl,
            "phone" to user.phone,
            "street" to user.street,
            "city" to user.city,
            "province" to user.province,
            "postalCode" to user.postalCode,
            "timestamp" to user.timestamp,
        )
    }

    private fun mapDataToUser(documentSnapshot: DocumentSnapshot): User {
        return User(
            id = documentSnapshot.id,
            name = documentSnapshot.getString("name") ?: "",
            email = documentSnapshot.getString("email") ?: "",
            thumbnailUrl = documentSnapshot.getString("thumbnailUrl") ?: "",
            phone = documentSnapshot.getString("phone") ?: "",
            street = documentSnapshot.getString("street") ?: "",
            city = documentSnapshot.getString("city") ?: "",
            province = documentSnapshot.getString("province") ?: "",
            postalCode = documentSnapshot.getString("postalCode") ?: "",
            timestamp = documentSnapshot.getTimestamp("timestamp") ?: null,
        )
    }

}