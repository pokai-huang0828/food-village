package com.example.foodvillage2205.view.screens

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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodvillage2205.R
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.*

@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(navController)},
        content = {Form()}
    )
}

@Composable
fun TopBar(navController: NavController) {
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
            text= stringResource(R.string.Profile_title),
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
fun Form() {
    val name = remember{ mutableStateOf("")}
    val email = remember { mutableStateOf("")}
    val phone = remember{ mutableStateOf("")}
    val password = remember{ mutableStateOf("")}
    val passwordVisible = remember { mutableStateOf(false)}

    Column(modifier = Modifier
        .height(700.dp)
        .verticalScroll(rememberScrollState())
        .padding(30.dp)){
        //Avatar
        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Box(contentAlignment = Alignment.TopEnd) {
                Image(//profile pic
                    painter = painterResource(R.drawable.rice_snacks),
                    stringResource(R.string.Profile_title),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape))

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier
                        .size(20.dp)
                        .offset(y = (-6).dp, x = 6.dp),
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
        Column (modifier = Modifier.fillMaxWidth())
        {
            Text(text = stringResource(R.string.Donator_name),
                fontSize = 20.sp,
                fontFamily = RobotoSlab,
                color= SecondaryColor,
                fontWeight = FontWeight.W900)
            Spacer(modifier = Modifier.height(5.dp))
            TextField (//Name
                value = name.value,
                onValueChange = {name.value = it},
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                label = {
                    Text(text = stringResource(R.string.Donator_name))
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = WhiteLight,
                    focusedIndicatorColor = SecondaryColor,
                    focusedLabelColor = SecondaryColor,
                    unfocusedLabelColor = SecondaryColor,
                    unfocusedIndicatorColor = SecondaryColor)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = stringResource(R.string.Email),
                fontSize = 20.sp,
                fontFamily = RobotoSlab,
                color= SecondaryColor,
                fontWeight = FontWeight.W900)
            Spacer(modifier = Modifier.height(5.dp))
            TextField (//Email
                value = email.value,
                onValueChange = {email.value = it},
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.Email))
                },
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = WhiteLight,
                    focusedIndicatorColor = SecondaryColor,
                    focusedLabelColor = SecondaryColor,
                    unfocusedIndicatorColor = SecondaryColor)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = stringResource(R.string.Phone),
                fontSize = 20.sp,
                fontFamily = RobotoSlab,
                color= SecondaryColor,
                fontWeight = FontWeight.W900)
            Spacer(modifier = Modifier.height(5.dp))
            TextField (//Phone Number
                value = phone.value,
                onValueChange = {phone.value = it},
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.Phone))
                },
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = WhiteLight,
                    focusedIndicatorColor = SecondaryColor,
                    focusedLabelColor = SecondaryColor,
                    unfocusedIndicatorColor = SecondaryColor)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = stringResource(R.string.Password),
                fontSize = 20.sp,
                fontFamily = RobotoSlab,
                color= SecondaryColor,
                fontWeight = FontWeight.W900)
            Spacer(modifier = Modifier.height(5.dp))
            TextField (//password
                value = password.value,
                onValueChange = {password.value = it},
                visualTransformation = if(passwordVisible.value)
                    VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.Password))
                },
                maxLines = 1,
                trailingIcon = {
                    val image = if (passwordVisible.value)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = {
                        passwordVisible.value = !passwordVisible.value
                    }) {
                        Icon(imageVector  = image, "")
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = WhiteLight,
                    focusedIndicatorColor = SecondaryColor,
                    focusedLabelColor = SecondaryColor,
                    unfocusedIndicatorColor = SecondaryColor)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {

            //view history button
            Button(onClick = {},
                modifier = Modifier
                    .padding(top = 15.dp)
                    .width(200.dp)
                    .height(50.dp),
                shape = Shapes.medium,
                colors = ButtonDefaults.buttonColors(SecondaryColor),
                contentPadding = PaddingValues(5.dp)
            ){
                Text(text = stringResource(R.string.History),
                    fontFamily = RobotoSlab,
                    color = White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W900)
            }

            Spacer(modifier = Modifier.height(5.dp))

            //update button
            Button(onClick = {},
                modifier = Modifier
                    .padding(top = 15.dp)
                    .width(200.dp)
                    .height(50.dp),
                shape = Shapes.medium,
                colors = ButtonDefaults.buttonColors(SecondaryColor),
                contentPadding = PaddingValues(5.dp)
            ){
                Text(text = stringResource(R.string.Update),
                    fontFamily = RobotoSlab,
                    color = White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W900)
            }
        }
    }
}