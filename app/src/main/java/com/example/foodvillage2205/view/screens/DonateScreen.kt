package com.example.foodvillage2205.view.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.foodvillage2205.Auth
import com.example.foodvillage2205.R
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.repositories.FireStorageRepo
import com.example.foodvillage2205.model.repositories.PostRepository
import com.example.foodvillage2205.model.repositories.UserRepository
import com.example.foodvillage2205.model.responses.Resource
import com.example.foodvillage2205.view.composables.CameraCapture
import com.example.foodvillage2205.view.composables.Drawer
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.*
import com.example.foodvillage2205.viewmodels.PostViewModelFactory
import com.example.foodvillage2205.viewmodels.PostsViewModel
import com.example.foodvillage2205.viewmodels.UserViewModel
import com.example.foodvillage2205.viewmodels.UserViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@Composable
fun DonateScreen(navController: NavController, auth: Auth) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(
            rememberDrawerState(initialValue = DrawerValue.Closed)
    )

    Scaffold(
            topBar = {
                TopBarDonateScreen(navController,
                        scope = scope,
                        scaffoldState = scaffoldState)
            },
            scaffoldState = scaffoldState,
            drawerContent = {
                Drawer(navController = navController, auth = auth)
            },
            content = { FormDonateScreen(navController = navController, auth = auth) }
    )
}

@Composable
fun TopBarDonateScreen(
        navController: NavController,
        scope: CoroutineScope,
        scaffoldState: ScaffoldState,
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

        Text(
                text = "New Post",
                color = White,
                fontFamily = RobotoSlab,
                fontSize = 30.sp,
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

@ExperimentalComposeUiApi
@SuppressLint("ProduceStateDoesNotAssignValue")
@ExperimentalPermissionsApi
@Composable
fun FormDonateScreen(
        navController: NavController,
        auth: Auth,
        postVM: PostsViewModel = viewModel(factory = PostViewModelFactory(PostRepository())),
        userVM: UserViewModel = viewModel(factory = UserViewModelFactory(UserRepository())),
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

    // Keyboard done button
    val (
            focusRequesterPhone,
            focusRequesterCity,
            focusRequesterProvince,
            focusRequesterPostCode,
    ) = FocusRequester.createRefs()
    val keyboardController = LocalSoftwareKeyboardController.current

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
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                        contentAlignment = Alignment.Center,
                )
                {
                    Image(
                            painter = painterResource(id = R.drawable.defaultimagepreview),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                    .padding(start = 58.dp)
                                    .height(200.dp)
                                    .width(200.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .shadow(elevation = 20.dp, RoundedCornerShape(15.dp), true),
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
                                    alignment = Alignment.Center,
                                    modifier = Modifier
                                            .padding(start = 58.dp)
                                            .height(200.dp)
                                            .width(200.dp)
                                            .clip(RoundedCornerShape(15.dp))
                                            .shadow(elevation = 20.dp, RoundedCornerShape(15.dp), true),
                            )
                        }
                    }
                }

                // Gallery Toggle
                OutlinedButton(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier
                                .size(38.dp)
                                .offset(y = (80).dp, x = (5).dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(SecondaryColor),
                        contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                            painter = painterResource(id = R.drawable.iv_gallery),
                            contentDescription = "Edit avatar",
                            tint = White,
                            modifier = Modifier.size(25.dp)
                    )
                }

                // Camara Toggle
                OutlinedButton(
                        onClick = { showCameraScreen = !showCameraScreen },
                        modifier = Modifier
                                .size(38.dp)
                                .offset(y = (30).dp, x = (-31).dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(SecondaryColor),
                        contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                            painter = painterResource(id = R.drawable.iv_camera),
                            contentDescription = "toggle camara",
                            tint = White,
                            modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            //Inputs
            Column(modifier = Modifier.fillMaxWidth())
            {
                //Post Name
                Text(
                        text = stringResource(R.string.details),
                        fontSize = 20.sp,
                        fontFamily = RobotoSlab,
                        color = SecondaryColor,
                        fontWeight = FontWeight.W900
                )
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                                .fillMaxWidth(),
                        label = {
                            Text(text = "Title")
                        },
                        singleLine = true,
                        maxLines = 1,
                        colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = WhiteLight,
                                focusedIndicatorColor = SecondaryColor,
                                focusedLabelColor = SecondaryColor,
                                unfocusedIndicatorColor = SecondaryColor
                        ),
                        keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                                onDone = { keyboardController?.hide() }
                        ),
                )
                if (name.isEmpty()) {
                    Text(
                            "Title is required.",
                            color = Danger,
                    )
                } else {

                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                        value = details,
                        onValueChange = { details = it },
                        modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                        label = {
                            Text(text = "Details")
                        },
                        maxLines = 5,
                        colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = WhiteLight,
                                focusedIndicatorColor = SecondaryColor,
                                focusedLabelColor = SecondaryColor,
                                unfocusedIndicatorColor = SecondaryColor
                        )
                )
                if (details.isEmpty()) {
                    Text(
                            "Details are required.",
                            color = Danger,
                    )
                } else {
                }

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
                OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier
                                .fillMaxWidth(),
                        label = {
                            Text(text = stringResource(R.string.email))
                        },
                        singleLine = true,
                        maxLines = 1,
                        colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = WhiteLight,
                                focusedIndicatorColor = SecondaryColor,
                                focusedLabelColor = SecondaryColor,
                                unfocusedIndicatorColor = SecondaryColor
                        ),
                        keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next

                        ),
                        keyboardActions = KeyboardActions(
                                onNext = { focusRequesterPhone.requestFocus() }
                        ),
                )

                //Phone Number
                OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequesterPhone),
                        label = {
                            Text(text = stringResource(R.string.phone))
                        },
                        singleLine = true,
                        maxLines = 1,
                        colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = WhiteLight,
                                focusedIndicatorColor = SecondaryColor,
                                focusedLabelColor = SecondaryColor,
                                unfocusedIndicatorColor = SecondaryColor
                        ),
                        keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                                onDone = { keyboardController?.hide() }
                        ),
                )
                if (phone.isEmpty()) {
                    Text(
                            "Phone number is required.",
                            color = Danger,
                    )
                } else {
                }
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
                OutlinedTextField(
                        value = street,
                        onValueChange = { street = it },
                        modifier = Modifier
                                .fillMaxWidth(),
                        label = {
                            Text(text = stringResource(R.string.Street))
                        },
                        singleLine = true,
                        maxLines = 1,
                        colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = WhiteLight,
                                focusedIndicatorColor = SecondaryColor,
                                focusedLabelColor = SecondaryColor,
                                unfocusedIndicatorColor = SecondaryColor
                        ),
                        keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                                onNext = { focusRequesterCity.requestFocus() }
                        ),
                )
                if (street.isEmpty()) {
                    Text(
                            "Address is required.",
                            color = Danger,
                    )
                } else {
                }

                Row {
                    //City
                    OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .focusRequester(focusRequesterCity),
                            label = {
                                Text(text = stringResource(R.string.City))
                            },
                            singleLine = true,
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = WhiteLight,
                                    focusedIndicatorColor = SecondaryColor,
                                    focusedLabelColor = SecondaryColor,
                                    unfocusedIndicatorColor = SecondaryColor
                            ),
                            keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                    onNext = { focusRequesterProvince.requestFocus() }
                            ),
                    )

                    //Province
                    OutlinedTextField(
                            value = province,
                            onValueChange = { province = it },
                            modifier = Modifier
                                    .padding(start = 5.dp)
                                    .fillMaxWidth()
                                    .focusRequester(focusRequesterProvince),
                            label = {
                                Text(text = stringResource(R.string.Province))
                            },
                            singleLine = true,
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = WhiteLight,
                                    focusedIndicatorColor = SecondaryColor,
                                    focusedLabelColor = SecondaryColor,
                                    unfocusedIndicatorColor = SecondaryColor
                            ),
                            keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                    onNext = { focusRequesterPostCode.requestFocus() }
                            ),
                    )
                }

                //Postal Code
                OutlinedTextField(
                        value = postalCode,
                        onValueChange = { postalCode = it },
                        modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequesterPostCode),
                        label = {
                            Text(text = stringResource(R.string.Postal))
                        },
                        singleLine = true,
                        maxLines = 1,
                        colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = WhiteLight,
                                focusedIndicatorColor = SecondaryColor,
                                focusedLabelColor = SecondaryColor,
                                unfocusedIndicatorColor = SecondaryColor
                        ),
                        keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                                onDone = { keyboardController?.hide() }
                        ),
                )
                if (postalCode.isEmpty()) {
                    Text(
                            "PostalCode is required.",
                            color = Danger,
                    )
                } else {
                    Text("")
                }

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
) {
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

