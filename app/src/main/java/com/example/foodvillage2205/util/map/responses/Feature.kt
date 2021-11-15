/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-14 15:19:42
 * @ Description: TODO
 */

package com.example.foodvillage2205.util.map.responses

data class Feature(
    val address: String,
    val bbox: List<Double>,
    val center: List<Double>,
    val context: List<Context>,
    val geometry: Geometry,
    val id: String,
    val matching_place_name: String,
    val matching_text: String,
    val place_name: String,
    val place_type: List<String>,
    val properties: Properties,
    val relevance: Int,
    val text: String,
    val type: String
)