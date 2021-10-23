package com.example.foodvillage2205.view.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.foodvillage2205.Auth
import com.example.foodvillage2205.R
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.repositories.FireStorageRepo
import com.example.foodvillage2205.model.repositories.UserRepository
import com.example.foodvillage2205.model.responses.Resource
import com.example.foodvillage2205.view.composables.gallery.EMPTY_IMAGE_URI
import com.example.foodvillage2205.view.composables.gallery.GallerySelect
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.*
import com.example.foodvillage2205.viewmodels.UserViewModel
import com.example.foodvillage2205.viewmodels.UserViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@ExperimentalPermissionsApi
@Composable
fun ProfileScreen(navController: NavController, auth: Auth) {
    Scaffold(
        topBar = { TopBar(navController, auth) },
        content = { Form(navController, auth) }
    )
}

@Composable
fun TopBar(
    navController: NavController,
    auth: Auth
) {
    Row(
        modifier = Modifier
            .background(SecondaryColor)
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Icon button to go back to Main Page
        IconButton(
            onClick = { navController.navigate(Route.MainScreen.route) },
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.CenterVertically)
        ) {
            Image(painterResource(R.drawable.food_village_logo_1), "")
        }

        //title
        Text(
            text = stringResource(R.string.Profile_title),
            color = White,
            fontSize = 30.sp,
            fontFamily = RobotoSlab,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        IconButton(
            onClick = { auth.signOut(navController) },
            modifier = Modifier
                .padding(5.dp)
                .size(45.dp)
                .clip(CircleShape)
                .background(PrimaryColor)
        ) {
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = "Logout",
                tint = White,
                modifier = Modifier
                    .size(28.dp)
            )
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun Form(
    navController: NavController,
    auth: Auth,
    userVM: UserViewModel = viewModel(factory = UserViewModelFactory(UserRepository()))
) {
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var thumbnailUrl by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf(Timestamp.now()) }

    // Set up for picking image from gallery
    val context = LocalContext.current
    var fireStorageRepo by remember { mutableStateOf(FireStorageRepo()) }
    var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }
    var imageChanged by remember { mutableStateOf(false) }
    var showGallery by remember { mutableStateOf(false) }
    var scope = rememberCoroutineScope()

    // Get User Info from firebase
    produceState(initialValue = false) {
        val resource = userVM.getUserById((auth.currentUser as FirebaseUser).uid)

        if (resource is Resource.Success<*>) {
            val user = resource.data as User
            id = user.id
            name = user.name
            email = user.email
            phone = user.phone
            thumbnailUrl = user.thumbnailUrl
            timestamp = user.timestamp!!
        }

        if (thumbnailUrl.isNotEmpty()) {
            imageUri = fireStorageRepo.getImage(thumbnailUrl).await()
        }
    }

    // Show Gallery Screen or Profile Screen
    if (showGallery) {
        GallerySelect(
            modifier = Modifier,
            onImageUri = { uri ->
                showGallery = false
                imageUri = uri
                imageChanged = true
            }
        )
    } else {
        Column(
            modifier = Modifier
                .height(700.dp)
                .verticalScroll(rememberScrollState())
                .padding(30.dp)
        ) {
            //Avatar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(contentAlignment = Alignment.TopEnd) {
                    Image(//profile pic, show default image if user does not has one
                        painter = if (thumbnailUrl.isEmpty() && imageUri === EMPTY_IMAGE_URI)
                            painterResource(R.drawable.default_image)
                        else
                            rememberImagePainter(imageUri),
                        stringResource(R.string.Profile_title),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                    )

                    // Icon to open gallery
                    OutlinedButton(
                        onClick = {
                            showGallery = true
                        },
                        modifier = Modifier
                            .size(30.dp)
                            .offset(y = (-6).dp, x = 6.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(SecondaryColor),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit avatar",
                            tint = White,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            //Inputs
            Column(modifier = Modifier.fillMaxWidth())
            {
                Text(
                    text = stringResource(R.string.Donator_name),
                    fontSize = 20.sp,
                    fontFamily = RobotoSlab,
                    color = SecondaryColor,
                    fontWeight = FontWeight.W900
                )
                Spacer(modifier = Modifier.height(5.dp))
                TextField(//Name
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxLines = 1,
                    label = {
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(R.string.Email),
                    fontSize = 20.sp,
                    fontFamily = RobotoSlab,
                    color = SecondaryColor,
                    fontWeight = FontWeight.W900
                )
                Spacer(modifier = Modifier.height(5.dp))
                TextField(//Email
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(R.string.Phone),
                    fontSize = 20.sp,
                    fontFamily = RobotoSlab,
                    color = SecondaryColor,
                    fontWeight = FontWeight.W900
                )
                Spacer(modifier = Modifier.height(5.dp))
                TextField(//Phone Number
                    value = phone,
                    onValueChange = { phone = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

//                Text(
//                    text = stringResource(R.string.Password),
//                    fontSize = 20.sp,
//                    fontFamily = RobotoSlab,
//                    color = SecondaryColor,
//                    fontWeight = FontWeight.W900
//                )
//                Spacer(modifier = Modifier.height(5.dp))
//                TextField(//password
//                    value = password,
//                    onValueChange = { password = it },
//                    visualTransformation = if (passwordVisible)
//                        VisualTransformation.None
//                    else PasswordVisualTransformation(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    label = {
//                    },
//                    maxLines = 1,
//                    trailingIcon = {
//                        val image = if (passwordVisible)
//                            Icons.Filled.Visibility
//                        else Icons.Filled.VisibilityOff
//
//                        IconButton(onClick = {
//                            passwordVisible = !passwordVisible
//                        }) {
//                            Icon(imageVector = image, "")
//                        }
//                    },
//                    colors = TextFieldDefaults.textFieldColors(
//                        backgroundColor = WhiteLight,
//                        focusedIndicatorColor = SecondaryColor,
//                        focusedLabelColor = SecondaryColor,
//                        unfocusedIndicatorColor = SecondaryColor
//                    )
//                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //view history button
                Button(
                    onClick = { navController.navigate(Route.DonateHistory.route) },
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .width(200.dp)
                        .height(50.dp),
                    shape = Shapes.medium,
                    colors = ButtonDefaults.buttonColors(SecondaryColor),
                    contentPadding = PaddingValues(5.dp)
                ) {
                    Text(
                        text = stringResource(R.string.History),
                        fontFamily = RobotoSlab,
                        color = White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                //update button:
                // upload image to firestorage if user has provided one
                // then update user info to firestore
                Button(
                    onClick = {
                        if (imageChanged) {
                            scope.launch {
                                fireStorageRepo.uploadImageToStorage(
                                    context,
                                    imageUri,
                                    imageUri.lastPathSegment!!
                                ).join()
                            }.invokeOnCompletion {
                                userVM.updateUser(
                                    User(
                                        id = id,
                                        name = name,
                                        email = email,
                                        phone = phone,
                                        thumbnailUrl = imageUri.lastPathSegment!!,
                                        timestamp = timestamp
                                    )
                                ) {
                                    // if resource is success
                                }
                            }
                        } else {
                            userVM.updateUser(
                                User(
                                    id = id,
                                    name = name,
                                    email = email,
                                    phone = phone,
                                    thumbnailUrl = thumbnailUrl,
                                    timestamp = timestamp
                                )
                            ) {
                                // if resource is success
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .width(200.dp)
                        .height(50.dp),
                    shape = Shapes.medium,
                    colors = ButtonDefaults.buttonColors(SecondaryColor),
                    contentPadding = PaddingValues(5.dp)
                ) {
                    Text(
                        text = stringResource(R.string.Update),
                        fontFamily = RobotoSlab,
                        color = White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900
                    )
                }
            }
        }
    }


}