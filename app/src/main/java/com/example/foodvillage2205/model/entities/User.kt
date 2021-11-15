/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:41:02
 * @ Description: File holds User data class
 */

package com.example.foodvillage2205.model.entities

import com.google.firebase.Timestamp

data class User(
    var id: String = "",
    val name: String = "",
    val email: String = "",
    val thumbnailUrl: String = "",
    val phone: String = "",
    val street: String = "",
    val city: String = "",
    val province: String = "",
    val postalCode: String = "",
    val timestamp: Timestamp? = Timestamp.now()
)
