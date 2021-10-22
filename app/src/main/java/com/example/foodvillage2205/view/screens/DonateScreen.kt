package com.example.foodvillage2205.view.screens


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
import androidx.compose.ui.graphics.Color
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
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DonateScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBarDonateScreen(navController)},
        content = {FormDonateScreen()}
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
        verticalAlignment = Alignment.CenterVertically) {
        //Icon button to go back to Main Page
        IconButton(onClick = {navController.navigate(Route.MainScreen.route)},
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.CenterVertically)) {
            Image(painterResource(R.drawable.food_village_logo_1), "")
        }

        //title
        Text(
            text= stringResource(R.string.D_title),
            color = White,
            fontSize = 30.sp,
            fontFamily = RobotoSlab,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(80.dp))
    }
}

@Composable
fun FormDonateScreen() {
    val name = remember { mutableStateOf("") }
    val details = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val street = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val province = remember { mutableStateOf("") }
    val postalCode = remember { mutableStateOf("") }

    // Add Image from google drive
    var imageUrl by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUrl = uri
    }

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
                (imageUrl?.let {
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
                )

                OutlinedButton(
                    onClick = {launcher.launch("image/*")},
                    modifier = Modifier
                        .size(20.dp)
                        .offset(y = (3).dp, x = 30.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(SecondaryColor),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit avatar",
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
                value = name.value,
                onValueChange = { name.value = it },
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
                value = details.value,
                onValueChange = { details.value = it },
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
                value = email.value,
                onValueChange = { email.value = it },
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
                value = phone.value,
                onValueChange = { phone.value = it },
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
                value = street.value,
                onValueChange = { street.value = it },
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
                value = city.value,
                onValueChange = { city.value = it },
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
                value = province.value,
                onValueChange = { province.value = it },
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
                value = postalCode.value,
                onValueChange = { postalCode.value = it },
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
                onClick = {},
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
