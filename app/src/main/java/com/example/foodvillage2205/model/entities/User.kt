package com.example.foodvillage2205.model.entities

import com.google.firebase.Timestamp

data class User(
    val id: String = "",
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
