package com.example.foodvillage

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.foodvillage.navigation.AuthNavigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        if (auth.currentUser == null) {
            RenderAuthNavigation()
        } else {
            RenderNavigation()
        }
    }

    private fun RenderAuthNavigation() {
        setContent {
            AuthNavigation(
                SignUp = { email, password -> SignUp(email, password) },
                SignIn = { email, password -> SignIn(email, password) }
            )
        }
    }

    private fun RenderNavigation() {
        setContent {
            Navigation(signOut = {
                auth.signOut()
                recreate()
            })
        }
    }

    private fun SignUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI
                    val user = auth.currentUser
                    Log.d("Auth", user!!.email!!)

                    RenderNavigation()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun SignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI
                    RenderNavigation()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}

