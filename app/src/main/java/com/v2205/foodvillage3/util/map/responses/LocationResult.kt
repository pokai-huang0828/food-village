/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-14 15:19:42
 * @ Description: This is a responses from mapbox search
 */

package com.v2205.foodvillage3.util.map.responses

import com.v2205.foodvillage3.util.map.responses.Feature

data class LocationResult(
    val attribution: String,
    val features: List<Feature>,
    val query: List<String>,
    val type: String
)