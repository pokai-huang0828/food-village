/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 11:37:52
 * @ Description: This file represents a clicked Food Item from the Home Screen
 */

package com.example.foodvillage2205.view.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.foodvillage2205.auth.Auth
import com.example.foodvillage2205.R
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.repositories.PostRepository
import com.example.foodvillage2205.model.repositories.UserRepository
import com.example.foodvillage2205.util.SessionPost
import com.example.foodvillage2205.util.TimestampToFormatedString
import com.example.foodvillage2205.view.composables.*
import com.example.foodvillage2205.view.theme.PrimaryColor
import com.example.foodvillage2205.view.theme.SecondaryColor
import com.example.foodvillage2205.view.theme.White
import com.example.foodvillage2205.viewmodels.PostViewModelFactory
import com.example.foodvillage2205.viewmodels.PostsViewModel
import com.example.foodvillage2205.viewmodels.UserViewModel
import com.example.foodvillage2205.viewmodels.UserViewModelFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@Composable
fun DetailScreen(navController: NavController, postId: String?, auth: Auth) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(
        rememberDrawerState(initialValue = DrawerValue.Closed)
    )
    /** The content consists only of [TopBarDetail] [Drawer] and [FoodDetailList] where we may see the details for the exact food item */
    Scaffold(
        topBar = {
            TopBarDetail(
                navController,
                scope = scope,
                scaffoldState = scaffoldState
            )
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            Drawer(navController = navController, auth = auth)
        },
        content = {
            FoodDetailList(postId = postId, navController = navController)
        }
    )
}

@Composable
fun TopBarDetail(
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
        verticalAlignment = Alignment.CenterVertically,

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
            text = "Detail",
            color = White,
            fontSize = 30.sp,
            fontFamily = RobotoSlab,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(end = 15.dp)
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
@Composable
fun FoodDetailList(
    postVM: PostsViewModel = viewModel(factory = PostViewModelFactory(PostRepository())),
    userVM: UserViewModel = viewModel(factory = UserViewModelFactory(UserRepository())),
    postId: String?,
    navController: NavController,
) {
    /** Getting Post for this screen and Its User */
    var user by remember { mutableStateOf(User()) }
    val post = produceState(initialValue = Post()) {
        if (postId != null) {
            postVM.getPostById(postId) { resPost ->
                value = resPost.data as Post
                SessionPost.setSessionPost(value.apply { id = postId })
                userVM.getUserById(value.userId) { resUser ->
                    user = resUser.data as User
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconTest(user)
        FoodDetail(post.value)
        Spacer(modifier = Modifier.padding(bottom = 10.dp))
        /** If the User and the Owner of this Post
         * he/she allowed to Edit it */
        if (post.value.userId == Firebase.auth.currentUser?.uid) {
            /** Post does not has an applicant */
            if (post.value.appliedUserID == "") {
                DefaultBtn(
                    btnText = stringResource(R.string.Edit),
                    navController = navController,
                    enabled = true,
                )
                Spacer(modifier = Modifier.padding(bottom = 10.dp))

            } else {
                /** Post has an applicant */
                val userById = produceState(initialValue = User()) {
                    userVM.getUserById(id = post.value.appliedUserID) { resPost ->
                      value = resPost.data as User
                    }
                }
                Column {
                    Text(
                        text = "Applicant's Name:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                        color = PrimaryColor,
                        fontFamily = RobotoSlab,
                    )
                    Text(
                        text = if(userById.value.name == "") "No Name provided" else userById.value.name,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        fontFamily = RobotoSlab,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                    Text(
                        text = "Applicant's Email:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                        color = PrimaryColor,
                        fontFamily = RobotoSlab,
                    )
                    Text(
                        text = userById.value.email,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        fontFamily = RobotoSlab,
                        modifier = Modifier.padding(start = 15.dp)
                    )

                    Text(
                        text = "Applicant's Phone Num:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                        color = PrimaryColor,
                        fontFamily = RobotoSlab,
                    )
                    Text(
                        text = if(userById.value.phone == "") "No Phone number provided" else userById.value.phone,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        fontFamily = RobotoSlab,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }


            }
            DefaultBtn(
                postVM = postVM,
                btnText = stringResource(R.string.Delete),
                navController = navController,
                enabled = true,
            )
        } else {
            if (post.value.appliedUserID == "") {
                DefaultBtn(
                    postVM = postVM,
                    btnText = stringResource(R.string.Apply),
                    navController = navController,
                    enabled = true,
                )
            } else {
                DefaultBtn(
                    postVM = postVM,
                    btnText = stringResource(R.string.Undo),
                    navController = navController,
                    enabled = true,
                )
            }
        }
        Spacer(modifier = Modifier.padding(bottom = 50.dp))
    }
}

@Composable
fun IconTest(user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .padding(end = 15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = if (user.thumbnailUrl.isNotEmpty())
                rememberImagePainter(user.thumbnailUrl)
            else
                painterResource(R.drawable.default_image),
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
            text = user.name,
            fontWeight = FontWeight.Bold,
            fontSize = 29.sp,
            fontStyle = FontStyle.Italic,
            fontFamily = RobotoSlab,
            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
            color = PrimaryColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FoodDetail(
    post: Post,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Image(
            painter =
            if (post.imageUrl.isNotEmpty())
                rememberImagePainter(post.imageUrl)
            else
                painterResource(R.drawable.default_image),
            contentDescription = "header",
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .shadow(elevation = 20.dp, clip = true, shape = RoundedCornerShape(15.dp))
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.Center,
        )

        // Post Title
        Text(
            text = post.title,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
            color = PrimaryColor,
            textAlign = TextAlign.Start,
            fontFamily = RobotoSlab,
        )

        // Post Description
        Text(
            text = if (post.description.isEmpty()) "No Description" else post.description,
            fontWeight = FontWeight.Normal,
            fontFamily = RobotoSlab,
            fontSize = 17.sp,
            modifier = Modifier.padding(start = 15.dp)
        )

        Text(
            text = "Contact",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
            color = PrimaryColor,
            fontFamily = RobotoSlab,
        )

        // Telephone
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 15.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PhoneAndroid,
                contentDescription = "",
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            Text(
                text = if (post.phone.isEmpty()) "No Phone" else post.phone,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
                fontFamily = RobotoSlab,
            )

        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 15.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "",
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            Text(
                text = if (post.email.isEmpty()) "No Email" else post.email,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
                fontFamily = RobotoSlab,
            )

        }

        Text(
            text = "Post Time",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
            color = PrimaryColor,
            fontFamily = RobotoSlab,
        )

        Text(
            text = TimestampToFormatedString(post.timestamp),
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
            fontFamily = RobotoSlab,
            modifier = Modifier.padding(start = 15.dp)
        )


        Text(
            text = "Location",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
            color = PrimaryColor,
            fontFamily = RobotoSlab,
        )

        // Address
        Text(
            text = if (post.street.isEmpty()) "No Location" else "${post.street}\n" +
                    "${post.city} ${post.province}\n" + "${post.postalCode}",
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
            modifier = Modifier.padding(bottom = 5.dp, start = 15.dp),
            fontFamily = RobotoSlab,
        )

        // Map
        if (post.postalCode.isNotBlank() || post.street.isNotBlank()) {
            OnlyMapBox(
                mapSearch = "${post.street} ${post.city}",
            )
        }
    }
}

