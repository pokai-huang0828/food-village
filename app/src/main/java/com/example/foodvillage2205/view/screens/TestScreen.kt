package com.example.foodvillage2205.view.screens

import android.util.Log
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
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.repositories.PostRepository
import com.example.foodvillage2205.model.repositories.UserRepository
import com.example.foodvillage2205.model.responses.Resource
import com.example.foodvillage2205.view.composables.CameraCapture
import com.example.foodvillage2205.view.composables.CameraPreview
import com.example.foodvillage2205.view.composables.gallery.EMPTY_IMAGE_URI
import com.example.foodvillage2205.view.theme.ButtonPadding_16dp
import com.example.foodvillage2205.view.theme.PrimaryColor
import com.example.foodvillage2205.viewmodels.PostViewModelFactory
import com.example.foodvillage2205.viewmodels.PostsViewModel
import com.example.foodvillage2205.viewmodels.UserViewModelFactory
import com.example.foodvillage2205.viewmodels.UserViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.flow.asStateFlow

@ExperimentalAnimationApi
@Composable
fun TestScreen(
    userVM: UserViewModel = viewModel(factory = UserViewModelFactory(UserRepository())),
    postVM: PostsViewModel = viewModel(factory = PostViewModelFactory(PostRepository()))
) {
    // getPostById Example
//    var name = produceState(initialValue = "") {
//        val resource = postVM.getPostById("D94uOoNScTPwF6oPlJV0")
//
//        value = if (resource is Resource.Success<*>) {
//            (resource.data as Post).title
//        } else {
//            resource?.message.let {
//                resource.message as String
//            }
//        }
//    }
//
//    Text(
//        text = "Hello, ${name.value}!",
//        modifier = Modifier.padding(bottom = 8.dp),
//        style = MaterialTheme.typography.h5
//    )

    // Create Post Example
//    postVM.createPost(Post(title = "Post 4")){
//        if(it is Resource.Error){
//            Log.d("Debug", "Could not create a post")
//        }
//
//        if(it is Resource.Success){
//            Log.d("Debug", "created post ${it.data}")
//        }
//    }

//    PostList()
}


//@ExperimentalAnimationApi
//@Composable
//fun PostList(
//    postsViewModel: PostsViewModel = viewModel(factory = PostViewModelFactory(PostRepository()))
//) {
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        createPost(postsViewModel = postsViewModel)
//
//        // Example of get real time posts from firestore
//        when (val postsListResponse =
//            postsViewModel.postsStateFlow.asStateFlow().collectAsState().value) {
//
//            is Resource.Error<*> -> {
//                Text(text = "Please try after sometime")
//            }
//
//            is Resource.Success<*> -> {
//                val listOfPosts = postsListResponse.data as List<Post>
//
//                listOfPosts?.let {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                    ) {
//                        Text(
//                            text = "The Posts",
//                            style = TextStyle(
//                                fontSize = 28.sp,
//                                fontWeight = FontWeight.ExtraBold
//                            ),
//                            modifier = Modifier.padding(16.dp)
//                        )
//
//                        LazyColumn(modifier = Modifier.fillMaxHeight()) {
//                            items(listOfPosts.size) {
//
//                                Card(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(16.dp),
//                                    shape = RoundedCornerShape(16.dp)
//                                ) {
//                                    PostDetails(listOfPosts[it], postsViewModel)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun createPost(postsViewModel: PostsViewModel) {
//    var title by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//    var imageUrl by remember { mutableStateOf("") }
//
//    Column(
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Text(
//            text = "Create Post",
//            style = TextStyle(
//                fontSize = 28.sp,
//                fontWeight = FontWeight.ExtraBold
//            ),
//            modifier = Modifier.padding(16.dp)
//        )
//
//        OutlinedTextField(
//            value = title,
//            onValueChange = { title = it },
//            label = { Text("Title") }
//        )
//        OutlinedTextField(
//            value = description,
//            onValueChange = { description = it },
//            label = { Text("Description") }
//        )
//        OutlinedTextField(
//            value = imageUrl,
//            onValueChange = { imageUrl = it },
//            label = { Text("Image Url") }
//        )
//
//        Button(
//            onClick = {
//                postsViewModel.createPost(
//                    Post(
//                        title = title,
//                        description = description,
//                        imageUrl = imageUrl
//                    )
//                )
//
//                title = ""
//                description = ""
//                imageUrl = ""
//            },
//            colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryColor),
//            modifier = Modifier.padding(ButtonPadding_16dp)
//        ) {
//            Text(text = "Create Post", color = Color.White)
//        }
//    }
//}
//
//@ExperimentalAnimationApi
//@Composable
//fun PostDetails(post: Post, postsViewModel: PostsViewModel) {
//    var showPostDescription by remember { mutableStateOf(false) }
//    val postCoverImageSize by animateDpAsState(
//        targetValue =
//        if (showPostDescription) 50.dp else 80.dp
//    )
//
//    Column(modifier = Modifier.clickable {
//        showPostDescription = showPostDescription.not()
//    }) {
//        Row(modifier = Modifier.padding(12.dp)) {
//            post.imageUrl?.let {
//                Image(
//                    painter = rememberImagePainter(
//                        data = post.imageUrl,
//                    ),
//                    contentDescription = "Post thumbnail",
//                    contentScale = ContentScale.Fit,
//                    modifier = Modifier.size(postCoverImageSize)
//                )
//            }
//
//            Column {
//                Text(
//                    text = "Post ID: " + post.id, style = TextStyle(
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 18.sp
//                    )
//                )
//                Text(
//                    text = post.title, style = TextStyle(
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 18.sp
//                    )
//                )
//
//                Text(
//                    text = post.description, style = TextStyle(
//                        fontWeight = FontWeight.Light,
//                        fontSize = 12.sp
//                    )
//                )
//            }
//        }
//
//        AnimatedVisibility(visible = showPostDescription) {
//            Column {
//                Text(
//                    text = post.description, style = TextStyle(
//                        fontWeight = FontWeight.SemiBold,
//                        fontStyle = FontStyle.Italic
//                    ),
//                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
//                )
//                Text(
//                    text = "Delete this post", style = TextStyle(
//                        fontWeight = FontWeight.SemiBold,
//                        fontStyle = FontStyle.Italic
//                    ),
//                    modifier = Modifier
//                        .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
//                        .clickable { postsViewModel.deletePost(post) }
//                )
//            }
//        }
//    }
//
//}
