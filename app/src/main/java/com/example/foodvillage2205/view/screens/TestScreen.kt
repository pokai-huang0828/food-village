package com.example.foodvillage2205.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.repositories.PostsRepository
import com.example.foodvillage2205.model.responses.OnError
import com.example.foodvillage2205.model.responses.OnSuccess
import com.example.foodvillage2205.view.theme.ButtonPadding_16dp
import com.example.foodvillage2205.view.theme.PrimaryColor
import com.example.foodvillage2205.viewmodels.PostViewModelFactory
import com.example.foodvillage2205.viewmodels.PostsViewModel
import kotlinx.coroutines.flow.asStateFlow

@ExperimentalAnimationApi
@Composable
fun TestScreen() {
    PostList()
}

@ExperimentalAnimationApi
@Composable
fun PostList(
    postsViewModel: PostsViewModel = viewModel(factory = PostViewModelFactory(PostsRepository()))
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        createPost(postsViewModel = postsViewModel)

        when (val postsListResponse =
            postsViewModel.postsStateFlow.asStateFlow().collectAsState()
                .value) {

            is OnError -> {
                Text(text = "Please try after sometime")
            }

            is OnSuccess -> {
                val listOfPosts = postsListResponse.posts

                listOfPosts?.let {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "The Posts",
                            style = TextStyle(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold
                            ),
                            modifier = Modifier.padding(16.dp)
                        )

                        LazyColumn(modifier = Modifier.fillMaxHeight()) {
                            items(listOfPosts.size) {

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    PostDetails(listOfPosts[it], postsViewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun createPost(postsViewModel: PostsViewModel) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Create Post",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            ),
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Image Url") }
        )

        Button(
            onClick = {
                postsViewModel.createPost(
                    Post(
                        name = name,
                        description = description,
                        image = imageUrl
                    )
                )

                name = ""
                description = ""
                imageUrl = ""
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryColor),
            modifier = Modifier.padding(ButtonPadding_16dp)
        ) {
            Text(text = "Create Post", color = Color.White)
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun PostDetails(post: Post, postsViewModel: PostsViewModel) {
    var showPostDescription by remember { mutableStateOf(false) }
    val postCoverImageSize by animateDpAsState(
        targetValue =
        if (showPostDescription) 50.dp else 80.dp
    )

    Column(modifier = Modifier.clickable {
        showPostDescription = showPostDescription.not()
    }) {
        Row(modifier = Modifier.padding(12.dp)) {
            post.image?.let {
                Image(
                    painter = rememberImagePainter(
                        data = post.image,
                    ),
                    contentDescription = "Post thumbnail",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(postCoverImageSize)
                )
            }

            Column {
                Text(
                    text = "Post ID: " + post.id, style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
                Text(
                    text = post.name, style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )

                Text(
                    text = post.description, style = TextStyle(
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp
                    )
                )
            }
        }

        AnimatedVisibility(visible = showPostDescription) {
            Column {
                Text(
                    text = post.description, style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                )
                Text(
                    text = "Delete this post", style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                        .clickable { postsViewModel.deletePost(post) }
                )
            }
        }
    }

}
