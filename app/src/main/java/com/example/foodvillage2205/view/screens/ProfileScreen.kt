package com.example.foodvillage2205.view.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.foodvillage2205.view.composables.Drawer
import com.example.foodvillage2205.view.composables.gallery.EMPTY_IMAGE_URI
import com.example.foodvillage2205.view.composables.gallery.GallerySelect
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.*
import com.example.foodvillage2205.viewmodels.UserViewModel
import com.example.foodvillage2205.viewmodels.UserViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@ExperimentalPermissionsApi
@Composable
fun ProfileScreen(navController: NavController, auth: Auth) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(
        rememberDrawerState(initialValue = DrawerValue.Closed)
    )

    Scaffold(
        topBar = { TopBarProfile(navController,
            scope = scope,
            scaffoldState = scaffoldState) },
        scaffoldState = scaffoldState,
        drawerContent = {
            Drawer(navController = navController, auth = auth)
        },
        content = { Form(navController, auth) }
    )
}

@Composable
fun TopBarProfile(
    navController: NavController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
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
        IconButton(
            onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
            Icon(
                Icons.Filled.Menu,
                contentDescription = "",
                tint = Color.White
            )
        }

        //title
        Text(
            text = stringResource(R.string.Profile_title),
            color = White,
            fontSize = 30.sp,
            fontFamily = RobotoSlab,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(end = 10.dp)
        )

        // Back button
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(2.dp)
                .size(45.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIos,
                contentDescription = "Back",
                tint = White,
                modifier = Modifier
                    .size(30.dp)
            )
        }
    }
}

@SuppressLint("ProduceStateDoesNotAssignValue")
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
    var postalCode by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var thumbnailUrl by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf(Timestamp.now()) }

    // Set up for picking image from gallery
    val context = LocalContext.current
    var coroutineScope = rememberCoroutineScope()
    var fireStorageRepo by remember { mutableStateOf(FireStorageRepo()) }
    var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }
    var imageChanged by remember { mutableStateOf(false) }
    var showGallery by remember { mutableStateOf(false) }

    // Get User Info from firebase
    produceState(initialValue = false) {
        val resource = userVM.getUserById((auth.currentUser as FirebaseUser).uid)

        if (resource is Resource.Success<*>) {
            val user = resource.data as User
            id = user.id
            name = user.name
            email = user.email
            phone = user.phone
            postalCode = user.postalCode
            province = user.province
            street = user.street
            city = user.city
            thumbnailUrl = user.thumbnailUrl
            timestamp = user.timestamp!!
        }

        if (thumbnailUrl.isNotEmpty()) {
            imageUri = Uri.parse(thumbnailUrl)
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
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //Avatar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.TopEnd) {
                    //profile pic, show default image if user does not has one
                    Image(
                        painter =
                        if (thumbnailUrl.isEmpty() && imageUri === EMPTY_IMAGE_URI)
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
                            .offset(y = (117).dp, x = (-5).dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(SecondaryColor),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit avatar",
                            tint = White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Button(
                    onClick = { navController.navigate(Route.DonateHistory.route) },
                    modifier = Modifier
                        .padding(15.dp)
                        .width(160.dp)
                        .height(50.dp),
                    shape = Shapes.medium,
                    colors = ButtonDefaults.buttonColors(SecondaryColor),
                    contentPadding = PaddingValues(5.dp)
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Filled.History,
                            contentDescription = "",
                            tint = White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(
                            text = stringResource(R.string.History),
                            fontFamily = RobotoSlab,
                            color = White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W900,
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }

            //Inputs
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {

                //Name
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    maxLines = 1,
                    label = {
                        Text(
                            text = stringResource(R.string.Donator_name),
                            fontSize = 20.sp,
                            fontFamily = RobotoSlab,
                            color = SecondaryColor,
                            fontWeight = FontWeight.W900,
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )

                // Email

                TextField(//Email
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 10.dp),
                    label = {
                        Text(
                            text = stringResource(R.string.Email),
                            fontSize = 20.sp,
                            fontFamily = RobotoSlab,
                            color = SecondaryColor,
                            fontWeight = FontWeight.W900,
                        )
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )


                // Phone


                //Phone Number
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    label = {
                        Text(
                            text = stringResource(R.string.Phone),
                            fontSize = 20.sp,
                            fontFamily = RobotoSlab,
                            color = SecondaryColor,
                            fontWeight = FontWeight.W900
                        )
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )

                // Street

                Spacer(modifier = Modifier.height(5.dp))
                TextField(
                    value = street,
                    onValueChange = { street = it },
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    label = {
                        Text(
                            text = stringResource(R.string.Street),
                            fontSize = 20.sp,
                            fontFamily = RobotoSlab,
                            color = SecondaryColor,
                            fontWeight = FontWeight.W900
                        )
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )

                // City

                Spacer(modifier = Modifier.height(5.dp))
                TextField(
                    value = city,
                    onValueChange = { city = it },
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    label = {
                        Text(
                            text = stringResource(R.string.City),
                            fontSize = 20.sp,
                            fontFamily = RobotoSlab,
                            color = SecondaryColor,
                            fontWeight = FontWeight.W900
                        )
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )

                // Province

                TextField(
                    value = province,
                    onValueChange = { province = it },
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    label = {
                        Text(
                            text = stringResource(R.string.Province),
                            fontSize = 20.sp,
                            fontFamily = RobotoSlab,
                            color = SecondaryColor,
                            fontWeight = FontWeight.W900
                        )
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )

                // Postal Code

                TextField(
                    value = postalCode,
                    onValueChange = { postalCode = it },
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    label = {
                        Text(
                            text = stringResource(R.string.Postal),
                            fontSize = 20.sp,
                            fontFamily = RobotoSlab,
                            color = SecondaryColor,
                            fontWeight = FontWeight.W900
                        )
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

                Spacer(modifier = Modifier.height(5.dp))

                // update button:
                Button(
                    onClick = {
                        // upload image to fire storage if user has provided one
                        if (imageChanged) {

                            var imageDownloadUrl: String? = null

                            coroutineScope.launch {
                                fireStorageRepo.uploadImageToStorage(
                                    context,
                                    imageUri,
                                    imageUri.lastPathSegment!!
                                ) {
                                    imageDownloadUrl = it.toString()
                                }.join()

                            }.invokeOnCompletion {
                                userVM.updateUser(
                                    User(
                                        id = id,
                                        name = name,
                                        email = email,
                                        phone = phone,
                                        postalCode = postalCode,
                                        province = province,
                                        street = street,
                                        city = city,
                                        thumbnailUrl = imageDownloadUrl ?: "",
                                        timestamp = timestamp
                                    )
                                ) {
                                    // if resource is success
                                    if(it is Resource.Success){
                                        navController.navigate(Route.MainScreen.route){
                                            popUpTo(Route.MainScreen.route)
                                        }
                                    }
                                }
                            }

                        } else {
                            // if user did not change image
                            userVM.updateUser(
                                User(
                                    id = id,
                                    name = name,
                                    email = email,
                                    phone = phone,
                                    postalCode = postalCode,
                                    province = province,
                                    street = street,
                                    city = city,
                                    thumbnailUrl = thumbnailUrl,
                                    timestamp = timestamp
                                )
                            ) {
                                // if resource is success
                                if(it is Resource.Success){
                                    navController.navigate(Route.MainScreen.route){
                                        popUpTo(Route.MainScreen.route)
                                    }
                                }
                            }
                        }

                    },
                    modifier = Modifier
                        .padding(top = 1.dp)
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