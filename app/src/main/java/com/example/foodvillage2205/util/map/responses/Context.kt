/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-14 15:19:42
 * @ Description: This is a responses from mapbox search
 */

package com.example.foodvillage2205.util.map.responses

data class Context(
    val id: String,
    val short_code: String,
    val text: String,
    val wikidata: String
)