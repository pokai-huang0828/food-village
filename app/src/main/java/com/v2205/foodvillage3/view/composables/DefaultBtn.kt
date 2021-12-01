/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-14 02:22:48
 * @ Description: This file contains the Button with default style
 */

package com.v2205.foodvillage3.view.composables

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.v2205.foodvillage3.auth.Auth
import com.v2205.foodvillage3.model.entities.Post
import com.v2205.foodvillage3.model.repositories.FireStorageRepo
import com.v2205.foodvillage3.model.responses.Resource
import com.v2205.foodvillage3.util.SessionPost
import com.v2205.foodvillage3.view.navigation.Route
import com.v2205.foodvillage3.view.screens.RobotoSlab
import com.v2205.foodvillage3.view.theme.SecondaryColor
import com.v2205.foodvillage3.view.theme.Shapes
import com.v2205.foodvillage3.view.theme.White
import com.v2205.foodvillage3.viewmodels.PostsViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@Composable
fun DefaultBtn(
    imageUrl: Uri? = null,
    coroutineScope: CoroutineScope? = null,
    fireStorageRepo: FireStorageRepo? = null,
    context: Context? = null,
    auth: Auth? = null,
    name: String = "",
    details: String = "",
    email: String = "",
    phone: String = "",
    street: String = "",
    city: String = "",
    province: String = "",
    postalCode: String = "",
    postVM: PostsViewModel? = null,
    navController: NavController? = null,
    btnText: String = "",
    enabled: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                when (btnText) {
                    /** imageUrl !== null means that a User
                     * updated an Image and we need to Upload it
                     * and keep Its url in a database */
                    "Update" -> if (imageUrl !== null) {
                        // upload image to fireStorage
                        var imageDownloadUrl: String? = null

                        coroutineScope?.launch {
                            fireStorageRepo?.uploadImageToStorage(
                                context!!,
                                imageUrl,
                                imageUrl.lastPathSegment!!
                            ) {
                                imageDownloadUrl = it.toString()
                            }?.join()

                        }?.invokeOnCompletion { fireStorageException ->
                            val post = Post(
                                id = SessionPost.getSessionPost().id,
                                title = name,
                                description = details,
                                email = email,
                                phone = phone,
                                street = street,
                                city = city,
                                province = province,
                                postalCode = postalCode,
                                userId = (auth?.currentUser as FirebaseUser).uid,
                                imageUrl = imageDownloadUrl ?: ""
                            )
                            // upload post to firestore
                            if (fireStorageException === null) {
                                postVM?.updatePost(post) { resource ->
                                    // if resource is success, return to Main Screen
                                    if (resource is Resource.Success) {
                                        navController?.navigate(Route.MainScreen.route)
                                    }
                                }
                            }
                        }
                    } else {
                        /** imageUrl is null and no need to upload the Image
                         * one more time - just updating the Post in database */
                        val post = Post(
                            id = SessionPost.getSessionPost().id,
                            title = name,
                            description = details,
                            email = email,
                            phone = phone,
                            street = street,
                            city = city,
                            province = province,
                            postalCode = postalCode,
                            userId = (auth?.currentUser as FirebaseUser).uid,
                            imageUrl = SessionPost.getSessionPost().imageUrl
                        )
                        postVM?.updatePost(post) { resource ->
                            // if resource is success, return to Main Screen
                            if (resource is Resource.Success) {
                                navController?.navigate(Route.MainScreen.route)
                            }
                        }
                        SessionPost.enabled = false
                    }
                    /** Creating a new Post */
                    "Submit" -> if (imageUrl !== null) {
                        // upload image to firestorage
                        var imageDownloadUrl: String? = null

                        coroutineScope?.launch {
                            fireStorageRepo?.uploadImageToStorage(
                                context!!,
                                imageUrl,
                                imageUrl.lastPathSegment!!
                            ) {
                                imageDownloadUrl = it.toString()
                            }?.join()

                        }?.invokeOnCompletion { fireStorageException ->
                            val post = Post(
                                title = name,
                                description = details,
                                email = email,
                                phone = phone,
                                street = street,
                                city = city,
                                province = province,
                                postalCode = postalCode,
                                userId = (auth?.currentUser as FirebaseUser).uid,
                                imageUrl = imageDownloadUrl ?: ""
                            )
                            // upload post to firestore
                            if (fireStorageException === null) {
                                postVM?.createPost(post) { resource ->
                                    if (resource is Resource.Success) {
                                        navController?.navigate(Route.MainScreen.route)
                                    }
                                }
                            }
                        }
                    }
                    "Edit" -> {
                        SessionPost.enabled = true
                        navController?.navigate(Route.DonateScreen.route)
                    }
                    "Delete" -> {
                        postVM?.deletePost(SessionPost.getSessionPost()) { resource ->
                            if (resource is Resource.Success) {
                                navController?.navigate(Route.MainScreen.route)
                            }
                        }
                    }
                    /** A User pressed on Apply for food Button */
                    "Apply" -> {
                        postVM?.updatePost(
                            SessionPost.getSessionPost()
                                .apply { appliedUserID = Firebase.auth.currentUser!!.uid }) { res ->
                            if (res is Resource.Success) {
                                navController?.navigate(Route.ApplyHistory.route)
                            }
                        }
                    }
                    /** A User decided not to take food and
                     * he/she is removing food from the List */
                    "Undo" -> {
                        postVM?.updatePost(
                            SessionPost.getSessionPost()
                                .apply { appliedUserID = "" }) { res ->
                            if (res is Resource.Success) {
                                navController?.navigate(Route.ApplyHistory.route)
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .padding(top = 15.dp)
                .width(200.dp)
                .height(50.dp),
            shape = Shapes.medium,
            colors = ButtonDefaults.buttonColors(SecondaryColor),
            contentPadding = PaddingValues(5.dp),
            enabled = enabled,
        ) {
            Text(
                text = btnText,
                fontFamily = RobotoSlab,
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.W900
            )
        }
    }
}