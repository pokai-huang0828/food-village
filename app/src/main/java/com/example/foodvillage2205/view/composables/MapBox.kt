package com.example.foodvillage2205.view.composables

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.foodvillage2205.R
import com.example.foodvillage2205.model.responses.Resource
import com.example.foodvillage2205.util.map.MapService
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class Coordinates(val latitude: Double, val longitude: Double)

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun MapBox(
    mapSearch: String = "vancouver community college",
    onSearchError: () -> Unit,
    onSearchSuccess: () -> Unit,
    //    auth: Auth,
//    userVM: UserViewModel = viewModel(factory = UserViewModelFactory(UserRepository())
) {
    val coroutineScope = rememberCoroutineScope()
    val mapService = MapService() // to call mapbox Geocoding API

    var location by remember { mutableStateOf(Coordinates(0.0, 0.0)) }
    var mapBox by remember { mutableStateOf<MapboxMap?>(null) }
    var mapView by remember { mutableStateOf<MapView?>(null) }
//    var street by remember { mutableStateOf(mapSearch) }
    var pointAnnotationManager by remember { mutableStateOf<PointAnnotationManager?>(null) }

    // initial run
    LaunchedEffect(key1 = true) {
        var response = mapService.getLocationResult(
            searchText = mapSearch
        )

        if (response is Resource.Success) {
            Log.d("result", response.data?.features.toString())

            // check if response has coordinates
            if (response.data?.features!!.isEmpty()) {
                onSearchError()
                return@LaunchedEffect;
            }

            location = Coordinates(
                response.data?.features!![0].center[1], //latitude
                response.data?.features!![0].center[0] // longitude
            )

            addAnnotationToMap(mapBox, location, mapView, pointAnnotationManager)
            moveToLocation(mapBox, location)
            onSearchSuccess()

        } else {
            Log.d("error", (response as Resource.Error).message.toString())

            // display error msg
        }
    }

    Column {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            factory = { context ->
                MapView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    mapView = this
                    mapBox = this.getMapboxMap()

                    pointAnnotationManager =
                        mapView?.annotations?.createPointAnnotationManager(mapView!!)

                    addAnnotationToMap(mapBox, location, mapView, pointAnnotationManager)
                }
            }
        )

        Button(onClick = {
            coroutineScope.launch(Dispatchers.Main) {
                Log.d("result", mapSearch)

                var response = mapService.getLocationResult(
                    searchText = mapSearch
                )

                if (response is Resource.Success) {
                    Log.d("result", response.data?.features.toString())

                    // check if response has coordinates
                    if (response.data?.features!!.isEmpty()) {
                        onSearchError()
                        return@launch;
                    }

                    location = Coordinates(
                        response.data?.features!![0].center[1], //latitude
                        response.data?.features!![0].center[0] // longitude
                    )

                    addAnnotationToMap(mapBox, location, mapView, pointAnnotationManager)
                    moveToLocation(mapBox, location)
                    onSearchSuccess()

                } else {
                    Log.d("result", (response as Resource.Error).message.toString())
                    onSearchError()

                    // display error msg
                }
            }
        }) {
            Text("Find My Address")
        }
    }
}

fun moveToLocation(
    mapBox: MapboxMap?,
    location: Coordinates,
) {
    mapBox?.flyTo(
        CameraOptions.Builder()
            .center(Point.fromLngLat(location.longitude, location.latitude))
            .zoom(14.0)
            .bearing(0.0)
            .build()
    )
}

fun addAnnotationToMap(
    mapBox: MapboxMap?,
    location: Coordinates,
    mapView: MapView?,
    pointAnnotationManager: PointAnnotationManager?,
) {
    mapBox?.loadStyleUri(
        Style.MAPBOX_STREETS
    ) {
        mapView?.location?.updateSettings {
            this.enabled = true
            pulsingEnabled = true
        }

        var point = Point.fromLngLat(
            location.longitude,
            location.latitude
        )

        val bitmap = BitmapFactory.decodeResource(
            mapView?.resources, R.drawable.location_pin
        )

        val pointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap)

        pointAnnotationManager?.deleteAll()
        pointAnnotationManager?.create(pointAnnotationOptions)
    }
}