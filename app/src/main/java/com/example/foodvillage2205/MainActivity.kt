package com.example.foodvillage2205

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import android.content.Intent
import com.example.foodvillage2205.view.navigation.AuthNavigation
import com.example.foodvillage2205.view.navigation.Navigation
import com.example.foodvillage2205.view.screens.TestScreen

class MainActivity : ComponentActivity() {
    private lateinit var auth: Auth

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Auth(this, getString(R.string.default_web_client_id))

        setContent {
            when (auth.currentUser) {
                null -> AuthNavigation(
                    signInWithGoogle = { auth.signInWithGoogle(requestCode = GOOGLE_AUTH) },
                    signUpWithEmailAndPassword = { email, password ->
                        auth.signUpWithEmailAndPassword(
                            this,
                            email,
                            password
                        )
                    },
                    signInWithEmailAndPassword = { email, password ->
                        auth.signInWithEmailAndPassword(
                            this,
                            email,
                            password
                        )
                    }
                )
                else ->
                    Navigation(signOut = { auth.signOut(this) })
            }

            // For testing purpose
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
        private const val GOOGLE_AUTH = 9001
    }
}


