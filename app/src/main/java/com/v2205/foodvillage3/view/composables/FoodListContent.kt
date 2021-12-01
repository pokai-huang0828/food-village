/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-14 02:22:48
 * @ Description: This file contains a list of Food Items
 */

package com.v2205.foodvillage3.view.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.v2205.foodvillage3.model.entities.Post
import com.v2205.foodvillage3.model.repositories.PostRepository
import com.v2205.foodvillage3.model.responses.Resource
import com.v2205.foodvillage3.view.screens.RobotoSlab
import com.v2205.foodvillage3.view.theme.Gray
import com.v2205.foodvillage3.view.theme.PrimaryColor
import com.v2205.foodvillage3.viewmodels.PostViewModelFactory
import com.v2205.foodvillage3.viewmodels.PostsViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.v2205.foodvillage3.R
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
        /** This List of Food Items renders for Main/Home Screen */
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
        /** This List of Food Items renders for ApplyHistory Screen */
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
            if (filtered.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    /** This shows if list is empty */
                    Image(
                        painter = painterResource(id = R.drawable.food_village_logo_2),
                        contentDescription = "Post Image",
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        alpha = 0.2f,
                    )
                    Text(
                        text = "Your list is empty.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 5.dp)
                            .alpha(0.4f),
                        color = PrimaryColor,
                        fontFamily = RobotoSlab,
                    )
                }

            } else {
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
        /** This List of Food Items renders for DonateHistory Screen */
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
            if (filtered.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    /** This shows if list is empty */
                    Image(
                        painter = painterResource(id = R.drawable.food_village_logo_2),
                        contentDescription = "Post Image",
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        alpha = 0.2f,
                    )
                    Text(
                        text = "Your list is empty.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 5.dp)
                            .alpha(0.4f),
                        color = PrimaryColor,
                        fontFamily = RobotoSlab,
                    )
                }
            } else {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .background(Gray)
                ) {
                    items(items = filtered) { post ->
                        val listItem: Post = post
                        FoodListItem(listItem = listItem, navController,applicant = listItem.appliedUserID)
                    }
                }
            }
        }
    }
}
