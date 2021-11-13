package com.example.foodvillage2205.util.map

import com.example.foodvillage2205.util.map.responses.LocationResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MapApi {

    @GET("geocoding/v5/mapbox.places/{search_text}.json?")
    suspend fun getLocationResult(
        @Path("search_text") searchText: String,
        @Query("access_token") accessToken: String,
        @Query("limit") limit: Int,
    ): LocationResult
}