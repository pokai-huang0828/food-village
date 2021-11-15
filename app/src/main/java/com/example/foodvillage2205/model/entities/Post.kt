/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 11:37:52
 * @ Description: File holds Post data class
 */

package com.example.foodvillage2205.model.entities

import com.google.firebase.Timestamp

data class Post(
    var id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val timestamp: Timestamp? = Timestamp.now(),
    val userId: String = "",
    val email: String = "",
    val phone: String = "",
    val street: String = "",
    val city: String = "",
    val province: String = "",
    val postalCode: String = "",
    var appliedUserID: String = "",
)