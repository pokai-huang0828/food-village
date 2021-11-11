package com.example.foodvillage2205.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.foodvillage2205.Auth
import com.example.foodvillage2205.R
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.responses.Resource
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.navigation.RouteArgs.Companion.postId
import com.example.foodvillage2205.view.screens.RobotoSlab
import com.example.foodvillage2205.view.theme.PrimaryColor
import com.example.foodvillage2205.view.theme.SecondaryColor
import com.example.foodvillage2205.view.theme.White
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Drawer(
    navController: NavController,
    auth: Auth,
) {

    Column(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
            ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = Modifier
            .background(SecondaryColor)
            .fillMaxWidth(),
            contentAlignment = Alignment.Center

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(20.dp)
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.food_village_logo_2),
//                    contentDescription = "",
//                    modifier = Modifier.size(125.dp)
//                )
                UserImage(auth)
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),

            ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { navController.navigate(Route.MainScreen.route) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.padding(10.dp))
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "Home",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { navController.navigate(Route.DonateScreen.route) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.padding(10.dp))
                Icon(
                    imageVector = Icons.Filled.AddCircleOutline,
                    contentDescription = "",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "New Post",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { navController.navigate(Route.ProfileScreen.route) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.padding(10.dp))
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { auth.signOut(navController)},
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.padding(10.dp))
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "Logout",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun UserImage(auth: Auth) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .padding(end = 15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(auth.currentUser?.photoUrl),
            contentDescription = "Post Image",
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(25.dp), true)
                .clip(RoundedCornerShape(25.dp))
                .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(25.dp)),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = auth.currentUser?.displayName?:"",
            fontWeight = FontWeight.Bold,
            fontSize = 29.sp,
            fontStyle = FontStyle.Italic,
            fontFamily = RobotoSlab,
            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
            color = White,
            textAlign = TextAlign.Center
        )
    }
}