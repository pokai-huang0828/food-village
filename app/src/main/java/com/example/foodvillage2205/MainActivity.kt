/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 11:37:52
 * @ Description: TODO
 */
package com.example.foodvillage2205

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.ActivityCompat
import com.example.foodvillage2205.auth.Auth
import com.example.foodvillage2205.util.PermissionForLocation
import com.example.foodvillage2205.view.navigation.Navigation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.*

@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    private lateinit var auth: Auth
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var  locationCallback: LocationCallback
    private var requestingLocationUpdates = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Auth(this, getString(R.string.default_web_client_id))

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                for (location in locationResult.locations){
                    Log.d(TAG, location.toString())
                }
            }
        }

        setContent {
            PermissionForLocation(){
                Navigation(auth)
            }
            // Just for testing
//            TestScreen()
        }
    }

    override fun onResume() {
        super.onResume()

        if (requestingLocationUpdates){
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()

        stopLocationUpdates()
    }

    private fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
        return locationRequest
    }

    private fun startLocationUpdates() {
        val locationRequest = createLocationRequest()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @ExperimentalAnimationApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_AUTH) {
            auth.onGoogleSignInResult(data)
        }
    }

    companion object {
        const val GOOGLE_AUTH = 9001
        val TAG = "Location"
    }
}


