package com.example.foodvillage2205.view.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
import java.util.regex.Pattern

@ExperimentalComposeUiApi
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

@Composable
fun ErrorMessage(text:String){
    Text(text, color = Danger)
}

@ExperimentalComposeUiApi
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

    // Keyboard done button
    val (focusRequesterPhone,
        focusRequesterCity,
        focusRequesterProvince,
        focusRequesterPostCode, ) = FocusRequester.createRefs()
    val keyboardController = LocalSoftwareKeyboardController.current

    //validate
    val inValidEmail = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val postalCodePattern = Pattern.compile("^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]\$")
    val phonePattern = Pattern.compile("^[+]?[0-9]{10,13}\$")
    val inValidPhone = !phonePattern.matcher(phone).matches()
    val inValidPostalCode = !postalCodePattern.matcher(postalCode).matches()

    val isValid by derivedStateOf {
        !inValidEmail &&
        !inValidPhone &&
        !inValidPostalCode &&
        name.isNotBlank() &&
        street.isNotBlank() &&
        province.isNotBlank() &&
        city.isNotBlank()
    }

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
                .height(750.dp)
                .verticalScroll(rememberScrollState())
                .padding(10.dp),
        ) {
            //Avatar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center,
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
                            .clip(RoundedCornerShape(75.dp))
                            .shadow(elevation = 12.dp, shape = RoundedCornerShape(75.dp), true)
                            .border(
                                width = 2.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(75.dp)
                            ),
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
            }

            //Inputs
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            )
            {

                //Name
                Text(
                    text = "User Name",
                    fontSize = 20.sp,
                    fontFamily = RobotoSlab,
                    color = SecondaryColor,
                    fontWeight = FontWeight.W900,
                )
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {Text(text = "Name")},
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor
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
                if (name.isBlank()) ErrorMessage("user name can't be blank")
                Spacer(modifier = Modifier.height(8.dp))

                //Contact
                Text(
                    text = "Contact",
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
                    label = {Text(text = stringResource(R.string.email))},
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor,
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
                    label = {Text(text = stringResource(R.string.phone))},
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor
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
                Spacer(modifier = Modifier.height(8.dp))

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
                    label = {Text(text = stringResource(R.string.Street))},
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor
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
                if (street.isBlank()) ErrorMessage("street can't be blank")
                Spacer(modifier = Modifier.height(8.dp))

                Row() {
                    //City
                    Column{
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .focusRequester(focusRequesterCity),
                            label = {Text(text = stringResource(R.string.City))},
                            singleLine = true,
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = WhiteLight,
                                focusedIndicatorColor = SecondaryColor,
                                focusedLabelColor = SecondaryColor
                            ),
                            isError = city.isBlank(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusRequesterProvince.requestFocus() }
                            ),
                        )
                        if (city.isBlank()) ErrorMessage("city can't be blank")
                    }

                    //Province
                    Column {
                        OutlinedTextField(
                            value = province,
                            onValueChange = { province = it },
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .fillMaxWidth()
                                .focusRequester(focusRequesterProvince),
                            label = {Text(text = stringResource(R.string.Province))},
                            singleLine = true,
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = WhiteLight,
                                focusedIndicatorColor = SecondaryColor,
                                focusedLabelColor = SecondaryColor
                            ),
                            isError = province.isBlank(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusRequesterPostCode.requestFocus() }
                            ),
                        )
                        if (province.isBlank()) ErrorMessage("province can't be blank")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                //Postal Code
                OutlinedTextField(
                    value = postalCode,
                    onValueChange = { postalCode = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesterPostCode),
                    label = {Text(text = stringResource(R.string.Postal))},
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = WhiteLight,
                        focusedIndicatorColor = SecondaryColor,
                        focusedLabelColor = SecondaryColor
                    ),
                    isError = inValidPostalCode,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                )
                if (inValidPostalCode) ErrorMessage("invalid postal code")
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
                    enabled = isValid,
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