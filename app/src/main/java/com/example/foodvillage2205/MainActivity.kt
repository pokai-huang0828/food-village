package com.example.foodvillage2205

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import com.example.foodvillage2205.view.navigation.Navigation
import com.example.foodvillage2205.view.screens.TestScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class MainActivity : ComponentActivity() {
    private lateinit var auth: Auth

    @ExperimentalPermissionsApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Auth(this, getString(R.string.default_web_client_id))

        setContent {
//            Navigation(auth)

        // Just for testing
//            TestScreen()
        }
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
    }
}


