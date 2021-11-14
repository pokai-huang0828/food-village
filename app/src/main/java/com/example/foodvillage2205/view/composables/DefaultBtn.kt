package com.example.foodvillage2205.view.composables

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
import com.example.foodvillage2205.Auth
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.repositories.FireStorageRepo
import com.example.foodvillage2205.model.responses.Resource
import com.example.foodvillage2205.util.SessionPost
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.screens.RobotoSlab
import com.example.foodvillage2205.view.theme.SecondaryColor
import com.example.foodvillage2205.view.theme.Shapes
import com.example.foodvillage2205.view.theme.White
import com.example.foodvillage2205.viewmodels.PostsViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


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

        //Submit Button
        Button(
            onClick = {
                // Check if imageUrl has been set
                when (btnText) {
                    "Update" -> if (imageUrl !== null) {
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
                                    // if resource is success, return to Main Screen
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
                            // if resource is success, return to Main Screen
                            if (resource is Resource.Success) {
                                navController?.navigate(Route.MainScreen.route)
                            }
                        }
                    }
                    "Apply" -> {
                        navController?.navigate(Route.ApplyHistory.route)
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