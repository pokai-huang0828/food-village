package com.example.foodvillage2205.view.screens

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodvillage2205.R
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.*
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.repositories.FireStorageRepo
import com.example.foodvillage2205.model.repositories.PostRepository
import com.example.foodvillage2205.viewmodels.PostViewModelFactory
import com.example.foodvillage2205.viewmodels.PostsViewModel
import com.example.foodvillage2205.Auth
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.repositories.UserRepository
import com.example.foodvillage2205.model.responses.Resource
import com.example.foodvillage2205.view.composables.CameraCapture
import com.example.foodvillage2205.viewmodels.UserViewModel
import com.example.foodvillage2205.viewmodels.UserViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@Composable
fun DonateScreen(navController: NavController, auth: Auth) {
    Scaffold(
        topBar = { TopBarDonateScreen(navController) },
        content = { FormDonateScreen(navController = navController, auth = auth) }
    )
}

@Composable
fun TopBarDonateScreen(navController: NavController) {
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
            text = stringResource(R.string.D_title),
            color = White,
            fontSize = 30.sp,
            fontFamily = RobotoSlab,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(80.dp))
    }
}

@SuppressLint("ProduceStateDoesNotAssignValue")
@ExperimentalPermissionsApi
@Composable
fun FormDonateScreen(
    navController: NavController,
    auth: Auth,
    postVM: PostsViewModel = viewModel(factory = PostViewModelFactory(PostRepository())),
    userVM: UserViewModel = viewModel(factory = UserViewModelFactory(UserRepository()))
) {
    var name by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }

    // Add Image from google drive
    var imageUrl by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    // Set up for firestorage upload
    val coroutineScope = rememberCoroutineScope()
    val fireStorageRepo by remember { mutableStateOf(FireStorageRepo()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUrl = uri
    }

    // Get User Info from firebase to populate input fields
    produceState(initialValue = false) {
        val resource = userVM.getUserById((auth.currentUser as FirebaseUser).uid)

        if (resource is Resource.Success<*>) {
            val user = resource.data as User
            email = user.email
            phone = user.phone
            postalCode = user.postalCode
            province = user.province
            street = user.street
            city = user.city
        }
    }

    var showCameraScreen by remember { mutableStateOf(false) }

    if (showCameraScreen) {
        CameraScreen(
            showCamera = showCameraScreen,
            modifier = Modifier.fillMaxSize(),
            toggleCamera = { showCameraScreen = !showCameraScreen }
        ) {
            // call return the Uri of the photo taken
            imageUrl = it

            // close the camera screen
            showCameraScreen = !showCameraScreen
        }
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
                    Text(
                        text = stringResource(R.string.AddImage),
                        fontSize = 20.sp,
                        fontFamily = RobotoSlab,
                        color = SecondaryColor,
                        fontWeight = FontWeight.W900
                    )

                    imageUrl?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap.value =
                                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                        } else {
                            val source = ImageDecoder.createSource(context.contentResolver, it)
                            bitmap.value = ImageDecoder.decodeBitmap(source)
                        }

                        bitmap.value?.let { bitmap ->
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                stringResource(R.string.Profile_title),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(150.dp)
                            )
                        }
                    }

                    // Gallery Toggle
                    OutlinedButton(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier
                            .size(20.dp)
                            .offset(y = (3).dp, x = 30.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(SecondaryColor),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.iv_gallery),
                            contentDescription = "Edit avatar",
                            tint = White,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Camara Toggle
                    OutlinedButton(
                        onClick = { showCameraScreen = !showCameraScreen },
                        modifier = Modifier
                            .size(20.dp)
                            .offset(y = (3).dp, x = 60.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(SecondaryColor),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.iv_camera),
                            contentDescription = "toggle camara",
                            tint = White,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            //Inputs
            Column(modifier = Modifier.fillMaxWidth())
            {
                //Post Name
                Text(
                    text = stringResource(R.string.postName),
                    fontSize = 20.sp,
                    fontFamily = RobotoSlab,
                    color = SecondaryColor,
                    fontWeight = FontWeight.W900
                )
                Spacer(modifier = Modifier.height(5.dp))
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.foodName))
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

                //Details
                Text(
                    text = stringResource(R.string.details),
                    fontSize = 20.sp,
                    fontFamily = RobotoSlab,
                    color = SecondaryColor,
                    fontWeight = FontWeight.W900
                )
                Spacer(modifier = Modifier.height(5.dp))
                TextField(
                    value = details,
                    onValueChange = { details = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.food_details))
                    },
                    maxLines = 5,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                //Contact
                Text(
                    text = stringResource(R.string.pickUp_Contact),
                    fontSize = 20.sp,
                    fontFamily = RobotoSlab,
                    color = SecondaryColor,
                    fontWeight = FontWeight.W900
                )
                Spacer(modifier = Modifier.height(5.dp))
                //Email
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.email))
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )

                //Phone Number
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.phone))
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

                //Pick Up Location
                Text(
                    text = stringResource(R.string.pickUp_Location),
                    fontSize = 20.sp,
                    fontFamily = RobotoSlab,
                    color = SecondaryColor,
                    fontWeight = FontWeight.W900
                )
                Spacer(modifier = Modifier.height(5.dp))

                //Street
                TextField(
                    value = street,
                    onValueChange = { street = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.Street))
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )

                //City
                TextField(
                    value = city,
                    onValueChange = { city = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.City))
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )

                //Province
                TextField(
                    value = province,
                    onValueChange = { province = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.Province))
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )

                //Postal Code
                TextField(
                    value = postalCode,
                    onValueChange = { postalCode = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.Postal))
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
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //Submit Button
                Button(
                    onClick = {
                        // Check if imageUrl has been set
                        if (imageUrl !== null) {
                            // upload image to firestorage
                            var imageDownloadUrl: String? = null

                            coroutineScope.launch {
                                fireStorageRepo.uploadImageToStorage(
                                    context,
                                    imageUrl!!,
                                    imageUrl!!.lastPathSegment!!
                                ) {
                                    imageDownloadUrl = it.toString()
                                }.join()

                            }.invokeOnCompletion { fireStorageException ->
                                val post = Post(
                                    title = name,
                                    description = details,
                                    email = email,
                                    phone = phone,
                                    street = street,
                                    city = city,
                                    province = province,
                                    postalCode = postalCode,
                                    userId = (auth.currentUser as FirebaseUser).uid,
                                    imageUrl = imageDownloadUrl ?: ""
                                )

                                // upload post to firestore
                                if (fireStorageException === null) {
                                    postVM.createPost(post) { resource ->
                                        // if resource is success, return to Main Screen
                                        if (resource is Resource.Success) {
                                            navController.navigate(Route.MainScreen.route)
                                        }
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
                    contentPadding = PaddingValues(5.dp)
                ) {
                    Text(
                        text = stringResource(R.string.Submit),
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

@ExperimentalPermissionsApi
@Composable
fun CameraScreen(
    showCamera: Boolean,
    modifier: Modifier = Modifier,
    toggleCamera: () -> Unit,
    onPhotoTaken: (Uri) -> Unit,
){
    if (showCamera) {
        Box(modifier = modifier) {
            CameraCapture(
                modifier = modifier,
                onImageFile = { file ->
                    onPhotoTaken(file.toUri())
                }
            )
            Row(
                modifier = Modifier.align(Alignment.TopCenter),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .padding(4.dp),
                    onClick = {
                        toggleCamera()
                    }
                ) {
                    Text("Back")
                }
            }
        }
    }
}

