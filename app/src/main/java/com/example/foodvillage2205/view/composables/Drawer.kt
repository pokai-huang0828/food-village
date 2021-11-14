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
import androidx.compose.material.icons.filled.*
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
import com.example.foodvillage2205.view.theme.Shapes
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
            .shadow(elevation = 1.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = Modifier
            .background(SecondaryColor)
            .fillMaxWidth()
            .shadow(elevation = 1.dp),
            contentAlignment = Alignment.Center

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(20.dp)
            ) {
                UserImage(auth)
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
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
                    tint = PrimaryColor
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "Home",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = RobotoSlab,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.NavigateNext,
                        contentDescription = "",
                        tint = PrimaryColor
                    )
                }
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
                    tint = PrimaryColor
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "New Post",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = RobotoSlab,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.NavigateNext,
                        contentDescription = "",
                        tint = PrimaryColor
                    )
                }
            }

            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = "User",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = RobotoSlab,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 2.dp, start = 10.dp)
            )
            Spacer(modifier = Modifier.padding(5.dp))
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
                    tint = PrimaryColor
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = RobotoSlab,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.NavigateNext,
                        contentDescription = "",
                        tint = PrimaryColor
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { navController.navigate(Route.DonateHistory.route) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.padding(10.dp))
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = "",
                    tint = PrimaryColor
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "Post History",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = RobotoSlab,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.NavigateNext,
                        contentDescription = "",
                        tint = PrimaryColor
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { navController.navigate(Route.ApplyHistory.route) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.padding(10.dp))
                Icon(
                    imageVector = Icons.Filled.Fastfood,
                    contentDescription = "",
                    tint = PrimaryColor
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "Apply History",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = RobotoSlab,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.NavigateNext,
                        contentDescription = "",
                        tint = PrimaryColor
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                )
            {
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.padding(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable { auth.signOut(navController) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.padding(10.dp))
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "",
                        tint = PrimaryColor,
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = "Logout",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = RobotoSlab,
                        color = PrimaryColor,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun UserImage(auth: Auth) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (auth.currentUser?.displayName.isNullOrBlank()) {
            Image(
                painter = painterResource(id = R.drawable.food_village_logo_2),
                contentDescription = "Post Image",
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .shadow(elevation = 12.dp, shape = RoundedCornerShape(75.dp), true),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        } else {

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = rememberImagePainter(auth.currentUser?.photoUrl),
                    contentDescription = "Post Image",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .shadow(elevation = 12.dp, shape = RoundedCornerShape(50.dp), true)
                        .clip(RoundedCornerShape(50.dp))
                        .border(width = 2.dp, color = White, shape = RoundedCornerShape(50.dp)),
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.size(5.dp))

                Text(
                    text = "Welcome! \n"+ auth.currentUser?.displayName?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = RobotoSlab,
                    modifier = Modifier.padding(top = 1.dp),
                    color = White,
                )
            }
        }
    }
}