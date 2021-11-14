package com.example.foodvillage2205.util.map

import com.example.foodvillage2205.Constant
import com.example.foodvillage2205.Constant.mapbox_access_token
import com.example.foodvillage2205.model.responses.Resource
import com.example.foodvillage2205.util.map.responses.LocationResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    ): Resource<LocationResult>{
        val response = try {
            api.getLocationResult(searchText, accessToken, limit)
        }   catch (e: Exception){
            return Resource.Error("An unknown error! Try again!")
        }
        return Resource.Success(response)
    }
}