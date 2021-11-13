package com.example.foodvillage2205.view.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.repositories.PostRepository
import com.example.foodvillage2205.model.responses.Resource
import com.example.foodvillage2205.view.screens.RobotoSlab
import com.example.foodvillage2205.view.theme.Gray
import com.example.foodvillage2205.view.theme.PrimaryColor
import com.example.foodvillage2205.viewmodels.PostViewModelFactory
import com.example.foodvillage2205.viewmodels.PostsViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asStateFlow


@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@Composable
fun FoodListContent(
    navController: NavController,
    postsViewModel: PostsViewModel = viewModel(factory = PostViewModelFactory(PostRepository())),
    userRequest: String,
    screen: String
) {
    when (screen) {
        // Exact real time posts from firestore
        "Home" -> {
            when (val postsListResponse =
                postsViewModel.postsStateFlow.asStateFlow().collectAsState().value) {
                is Resource.Success<*> -> {
                    val foodItems = postsListResponse.data as List<*>
                    val filtered =
                        foodItems.filter { userRequest.lowercase() in (it as Post).title.lowercase() && it.appliedUserID == "" }
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(2),
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .background(Gray)
                    ) {
                        items(items = filtered) { post ->
                            val listItem: Post = post as Post
                            FoodListItem(listItem = listItem, navController)
                        }
                    }
                }
            }
        }
        "ApplyHistory" -> {
            val postVM: PostsViewModel = viewModel(factory = PostViewModelFactory(PostRepository()))
            val foodItems = produceState(initialValue = mutableListOf<Post>()) {
                postVM.getPostByUser(
                    field = "appliedUserID",
                    id = Firebase.auth.currentUser!!.uid
                ) { resPost ->
                    value = resPost.data as MutableList<Post>
                }
            }
            val filtered =
                foodItems.value.filter { userRequest.lowercase() in (it).title.lowercase() }
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .background(Gray)
            ) {
                items(items = filtered) { post ->
                    val listItem: Post = post
                    FoodListItem(listItem = listItem, navController)
                }
            }
        }
        "DonateHistory" -> {
            val postVM: PostsViewModel = viewModel(factory = PostViewModelFactory(PostRepository()))
            val foodItems = produceState(initialValue = mutableListOf<Post>()) {
                postVM.getPostByUser(
                    field = "userId",
                    id = Firebase.auth.currentUser!!.uid
                ) { resPost ->
                    value = resPost.data as MutableList<Post>
                }
            }
            val filtered =
                foodItems.value.filter { userRequest.lowercase() in (it).title.lowercase() }
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .background(Gray)
            ) {
                items(items = filtered) { post ->
                    val listItem: Post = post
                    FoodListItem(listItem = listItem, navController)
                }
            }
        }
    }
}