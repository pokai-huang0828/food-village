package com.example.foodvillage2205.util.map.responses

data class LocationResult(
    val attribution: String,
    val features: List<Feature>,
    val query: List<String>,
    val type: String
)