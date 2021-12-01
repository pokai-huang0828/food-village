/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-14 15:19:42
 * @ Description: This is a responses from mapbox search
 */

package com.v2205.foodvillage3.util.map.responses

data class Geometry(
    val coordinates: List<Double>,
    val type: String
)