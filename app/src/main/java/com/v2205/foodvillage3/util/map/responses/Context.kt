/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-14 15:19:42
 * @ Description: This is a responses from mapbox search
 */

package com.v2205.foodvillage3.util.map.responses

data class Context(
    val id: String,
    val short_code: String,
    val text: String,
    val wikidata: String
)