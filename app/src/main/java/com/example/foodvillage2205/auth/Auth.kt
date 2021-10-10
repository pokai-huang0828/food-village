package com.example.foodvillage2205.auth

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Auth {
    private var _auth: FirebaseAuth = Firebase.auth
    var currentUser: Any? = null

    init {
        // Initialize Firebase Auth
        currentUser = _auth.currentUser
    }

    fun SignUp(context: Activity, email: String, password: String) {
        _auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI
                    val user = _auth.currentUser
                    Log.d("Auth", user!!.email!!)

                    recreate(context)
                } else {
                    Toast.makeText(
                        context, "Sign Up failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun SignIn(context: Activity, email: String, password: String) {
        _auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI
                    recreate(context)
                } else {
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun SignOut(context: Activity) {
        _auth.signOut()
        currentUser = _auth.currentUser
        recreate(context)
    }
}