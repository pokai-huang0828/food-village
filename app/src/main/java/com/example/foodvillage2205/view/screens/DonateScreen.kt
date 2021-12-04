/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:41:02
 * @ Description: This file represents a Screen where a User
 * @ may donate food or edit the existing Food Item
 */

package com.example.foodvillage2205.view.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Patterns
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.foodvillage2205.auth.Auth
import com.example.foodvillage2205.R
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.repositories.FireStorageRepo
import com.example.foodvillage2205.model.repositories.PostRepository
import com.example.foodvillage2205.model.repositories.UserRepository
import com.example.foodvillage2205.model.responses.Resource
import com.example.foodvillage2205.util.SessionPost
import com.example.foodvillage2205.view.composables.*
import com.example.foodvillage2205.view.theme.SecondaryColor
import com.example.foodvillage2205.view.theme.White
import com.example.foodvillage2205.view.theme.WhiteLight
import com.example.foodvillage2205.viewmodels.PostViewModelFactory
import com.example.foodvillage2205.viewmodels.PostsViewModel
import com.example.foodvillage2205.viewmodels.UserViewModel
import com.example.foodvillage2205.viewmodels.UserViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@Composable
fun DonateScreen(navController: NavController, auth: Auth) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(
        rememberDrawerState(initialValue = DrawerValue.Closed)
    )
    /** The content consists only of [TopBar] [Drawer] and [FormDonateScreen] for posting new food items */

    Scaffold(
        topBar = {
            TopBarDonateScreen(
                navController,
                scope = scope,
                scaffoldState = scaffoldState
            )
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
        /** Depending whether a User clicked on Existing Item or It is a New one */
        Text(
            text = if (SessionPost.enabled) "Edit Mode" else "New Post",
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

@ExperimentalCoroutinesApi
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
    var name by remember { mutableStateOf(if (SessionPost.enabled) SessionPost.getSessionPost().title else "") }
    var details by remember { mutableStateOf(if (SessionPost.enabled) SessionPost.getSessionPost().description else "") }
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

    //validate
    val inValidEmail = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val postalCodePattern = Pattern.compile("^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]\$")
    val phonePattern = Pattern.compile("^[+]?[0-9]{10,13}\$")
    val inValidPhone = !phonePattern.matcher(phone).matches()
    val inValidPostalCode = !postalCodePattern.matcher(postalCode).matches()
    var isValidAddress by remember { mutableStateOf(false) }
    var isDirty by remember { mutableStateOf(true) }
    var imageUpdated by remember { mutableStateOf(false) }

    val isValid by derivedStateOf {
        !inValidEmail &&
                !inValidPhone &&
                !inValidPostalCode &&
                name.isNotBlank() &&
                details.isNotBlank() &&
                street.isNotBlank() &&
                province.isNotBlank() &&
                city.isNotBlank() &&
                isValidAddress &&
                !isDirty &&
                imageUpdated
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUrl = uri
    }

    // Get User Info from firebase to populate input fields
    produceState(initialValue = false) {
        userVM.getUserById((auth.currentUser as FirebaseUser).uid) { resource ->
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                            painter = rememberImagePainter(
                                data = if (SessionPost.enabled) SessionPost.getSessionPost().imageUrl
                                else R.drawable.defaultimagepreview
                            ),
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

                if (SessionPost.enabled || imageUrl != null) {
                    imageUpdated = true
                } else {
                    imageUpdated = false
                    ErrorMessage(errorMessage = "Picture is required")
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
                //Item Title
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Title") },
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    ),
                    isError = name.isBlank(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                )
                if (name.isBlank()) ErrorMessage("Title can't be blank")

                Spacer(modifier = Modifier.height(10.dp))

                // Details
                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    label = {
                        Text(text = "Details")
                    },
                    isError = details.isEmpty(),
                    maxLines = 5,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
                        unfocusedIndicatorColor = SecondaryColor
                    )
                )
                if (details.isEmpty()) ErrorMessage("Details can't be blank")

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
                    isError = inValidEmail,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next

                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequesterPhone.requestFocus() }
                    ),
                )
                if (inValidEmail) ErrorMessage("invalid email")

                Spacer(modifier = Modifier.height(8.dp))

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
                    isError = inValidPhone,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                )
                if (inValidPhone) ErrorMessage("invalid phone number")

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
                    isError = street.isBlank(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequesterCity.requestFocus() }
                    ),
                )
                if (street.isBlank()) ErrorMessage("Street can't be blank")

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Column {
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
                            isError = city.isBlank(),
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
                        if (city.isBlank()) ErrorMessage("City can't be blank")
                    }


                    Column {
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
                            isError = province.isBlank(),
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
                        if (province.isBlank()) ErrorMessage("Province can't be blank")
                    }

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
                    isError = inValidPostalCode,
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
                if (inValidPostalCode) ErrorMessage("invalid postal code")
                Spacer(modifier = Modifier.height(10.dp))
            }

            // MapBox
            if (postalCode.isNotBlank() || street.isNotBlank()) {
                MapBox(
                    mapSearch = "$street $city",
                    onSearchError = {
                        // do something on Error
                        isValidAddress = false

                    }
                ) {
                    // do something on Success
                    isValidAddress = true
                    isDirty = false
                }
            }

            Spacer(modifier = Modifier.height(5.dp))
            /** Depending whether a User clicked on an existing Item or it is a new one */
            DefaultBtn(
                imageUrl,
                coroutineScope,
                fireStorageRepo,
                context,
                auth,
                name,
                details,
                email,
                phone,
                street,
                city,
                province,
                postalCode,
                postVM,
                navController,
                stringResource(if (SessionPost.enabled) R.string.Update else R.string.Submit),
                enabled = isValid,
            )
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

