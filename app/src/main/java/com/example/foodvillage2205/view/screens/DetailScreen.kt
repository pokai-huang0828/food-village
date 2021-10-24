package com.example.foodvillage2205.view.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.foodvillage2205.R
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.repositories.PostRepository
import com.example.foodvillage2205.model.repositories.UserRepository
import com.example.foodvillage2205.model.responses.Resource
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.PrimaryColor
import com.example.foodvillage2205.view.theme.SecondaryColor
import com.example.foodvillage2205.view.theme.White
import com.example.foodvillage2205.viewmodels.PostViewModelFactory
import com.example.foodvillage2205.viewmodels.PostsViewModel
import com.example.foodvillage2205.viewmodels.UserViewModel
import com.example.foodvillage2205.viewmodels.UserViewModelFactory

@Composable
fun DetailScreen(navController: NavController, postId: String?) {
    Scaffold(
        topBar = { TopBarDetail(navController) },
        content = { FoodDetailList(postId) }
    )
}

@Composable
fun TopBarDetail(navController: NavController) {
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
            text = "Detail",
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
fun FoodDetailList(
    postId: String?,
    postVM: PostsViewModel = viewModel(factory = PostViewModelFactory(PostRepository())),
    userVM: UserViewModel = viewModel(factory = UserViewModelFactory(UserRepository())),
) {
    var user by remember { mutableStateOf(User()) }
    val post = produceState(initialValue = Post()) {
        postId?.let {
            val resource = postVM.getPostById(postId)

            resource?.let {
                if (resource is Resource.Success) {
                    value = resource.data as Post

                    // Get Post owner info
                    val resource = userVM.getUserById(value.userId)
                    if (resource is Resource.Success) {
                        user = resource.data as User
                    }
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

        Spacer(modifier = Modifier.padding(bottom = 80.dp))
    }
}

@Composable
fun IconTest(user: User) {
    Row(
        modifier = Modifier
            .padding(horizontal = 85.dp)
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Image(
            painter = if (user.thumbnailUrl.isNotEmpty())
                rememberImagePainter(user.thumbnailUrl)
            else
                painterResource(R.drawable.default_image),
            contentDescription = "Some text",
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = user.name,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
            color = PrimaryColor,
        )
    }
}

@Composable
fun FoodDetail(
    post: Post
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter =
            if (post.imageUrl.isNotEmpty())
                rememberImagePainter(post.imageUrl)
            else
                painterResource(R.drawable.default_image),
            contentDescription = "header",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(160.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        // Post Title
        Text(
            text = post.title,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
            color = PrimaryColor,
        )

        // Post Description
        Text(
            text = post.description,
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
        )

        Text(
            text = "Contact",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
            color = PrimaryColor,
        )

        // Telephone
        Text(
            text = post.phone,
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
        )

        Text(
            text = "Location",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
            color = PrimaryColor,
        )

        // Address
        Text(
            text = "${post.street}\n" +
                    "${post.city} ${post.province}\n" +
                    "${post.postalCode}",
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        // Map
        Image(
            painter = painterResource(id = R.drawable.map),
            contentDescription = "header",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(160.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

    }
}