/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 11:37:52
 * @ Description: This file contains FireBase Authentication class
 */

package com.example.foodvillage2205.auth

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavController
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.repositories.UserRepository
import com.example.foodvillage2205.model.responses.Resource
import com.example.foodvillage2205.view.navigation.Route
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

/**
 * Represents authentication with [Firebase] via Google and Login + Password
 */
class Auth(var context: Activity, default_web_client_id: String) {

    private var googleSignInClient: GoogleSignInClient
    private var _auth: FirebaseAuth = Firebase.auth
    var currentUser: FirebaseUser? = null

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
                    val userRepo = UserRepository()
                    currentUser = _auth.currentUser

                    // Try to get user from firestore
                    CoroutineScope(IO).launch {
                        userRepo.getUserById(_auth.currentUser?.uid!!) { response ->
                            // if no user, create one
                            if (response is Resource.Error) {
                                userRepo.createUser(
                                    User(
                                        email = _auth.currentUser?.email!!,
                                        id = _auth.currentUser?.uid!!
                                    )
                                ) {
                                    // check response type here
                                }
                            }
                        }
                    }

                    // Restart the app without transition
                    context.finish()
                    context.startActivity(context.intent)
                    context.overridePendingTransition(0, 0)
                } else {
                    currentUser = null
                }
            }
    }

    @ExperimentalAnimationApi
    fun signUpWithEmailAndPassword(
        navController: NavController,
        email: String,
        password: String,
    ) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            _auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        currentUser = _auth.currentUser

                        // Save new user to firestore
                        val userRepo = UserRepository()
                        userRepo.createUser(
                            User(
                                email = _auth.currentUser?.email!!,
                                id = _auth.currentUser?.uid!!
                            )
                        ) {
                            if (it is Resource.Success) {
                                // Sign up and Save user successfully, update UI
                                navController.navigate(Route.MainScreen.route)
                            }
                        }

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
    }

    @ExperimentalAnimationApi
    fun signInWithEmailAndPassword(
        navController: NavController,
        email: String,
        password: String,
        onError: () -> Unit,
    ) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            _auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI
                        currentUser = _auth.currentUser

                        navController.navigate(Route.MainScreen.route)
                    } else {
                        // If sign in fails, display a message to the user.
                        onError()
                    }
                }
        }
    }

    fun signOut(navController: NavController) {
        _auth.signOut()
        currentUser = null

        navController.navigate(Route.SignInScreen.route)
    }
}


