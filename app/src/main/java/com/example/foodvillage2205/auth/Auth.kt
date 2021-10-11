package com.example.foodvillage2205

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import androidx.core.app.ActivityCompat.recreate
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Auth(var context: Activity, default_web_client_id: String) {

    private var googleSignInClient: GoogleSignInClient
    private var _auth: FirebaseAuth = Firebase.auth
    var currentUser: Any? = null

    init {
        // Initialize Firebase Auth
        currentUser = _auth.currentUser

        // Initialize gso
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(default_web_client_id)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun signInWithGoogle(requestCode: Int) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(context, signInIntent, requestCode, null)
    }

    @ExperimentalAnimationApi
    fun onGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

        try {
            val account = task.getResult(ApiException::class.java)!!

            // Google Sign In was successful, authenticate with Firebase
            firebaseAuthWithGoogle(account.idToken!!)

        } catch (e: ApiException) {
            // If sign in fails, display a message to the user.
            Toast.makeText(
                context,
                "Authentication failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @ExperimentalAnimationApi
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        _auth.signInWithCredential(credential)
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    currentUser = _auth.currentUser

                    recreate(context)
                } else {
                    currentUser = null
                }
            }
    }

    @ExperimentalAnimationApi
    fun signUpWithEmailAndPassword(context: Activity, email: String, password: String) {
        _auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI
                    currentUser = _auth.currentUser

                    recreate(context)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    @ExperimentalAnimationApi
    fun signInWithEmailAndPassword(context: Activity, email: String, password: String) {
        _auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI
                    currentUser = _auth.currentUser

                    recreate(context)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun signOut(context: Activity) {
        _auth.signOut()
        currentUser = _auth.currentUser

        recreate(context)
    }
}


