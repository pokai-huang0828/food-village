/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-14 15:19:42
 * @ Description: This is a map service file.
 */

package com.v2205.foodvillage3.util.map

import com.v2205.foodvillage3.Constant
import com.v2205.foodvillage3.Constant.mapbox_access_token
import com.v2205.foodvillage3.model.responses.Resource
import com.v2205.foodvillage3.util.map.responses.LocationResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** This is MapService to setup getLocationResult
 * Used by [com.example.foodvillage2205.view.composables.MapBox],
 * [com.example.foodvillage2205.view.composables.OnlyMapBox]*/
class MapService {
    private val api: MapApi

    init {
        api = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constant.mapbox_base_url)
            .build()
            .create(MapApi::class.java)
    }
    suspend fun getLocationResult(
        searchText: String,
        accessToken: String = mapbox_access_token,
        limit: Int = 1
    ): Resource<LocationResult> {
        val response = try {
            api.getLocationResult(searchText, accessToken, limit)
        }   catch (e: Exception){
            return Resource.Error("An unknown error! Try again!")
        }
        return Resource.Success(response)
    }
}