package com.example.foodvillage2205.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.FloatingActionButtonDefaults.elevation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.foodvillage2205.Auth
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.repositories.PostRepository
import com.example.foodvillage2205.model.responses.Resource
import com.example.foodvillage2205.util.SessionPost
import com.example.foodvillage2205.view.composables.Drawer
import com.example.foodvillage2205.view.composables.FoodListItem
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.*
import com.example.foodvillage2205.viewmodels.PostViewModelFactory
import com.example.foodvillage2205.viewmodels.PostsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun MainScreen(navController: NavController, auth: Auth) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(
        rememberDrawerState(initialValue = DrawerValue.Closed)
    )
    var userRequest by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.background(Gray),
        topBar = {
            TopBar(
                navController,
                scope = scope,
                scaffoldState = scaffoldState
            ) { userSearch ->
                userRequest = userSearch
            }
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            Drawer(navController = navController, auth = auth)
        },
        content = {
            FoodListContent(navController, userRequest = userRequest)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    SessionPost.enabled = false
                    navController.navigate(Route.DonateScreen.route)
                },
                backgroundColor = SecondaryColor,
                contentColor = Color.White,
                modifier = Modifier
                    .width(100.dp)
                    .height(45.dp),
                elevation = elevation(15.dp),
                shape = Shapes.large
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "FAB",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = "New",
                        fontSize = 18.sp,
                        fontFamily = RobotoSlab,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun TopBar(
    navController: NavController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    filterRequest: (String) -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    Column(Modifier.shadow(elevation = 5.dp)) {
        Row(
            modifier = Modifier
                .background(SecondaryColor)
                .fillMaxWidth()
                .height(60.dp)
                .padding(vertical = 3.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
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

            Text(
                text = "Food Village",
                color = White,
                fontFamily = RobotoSlab,
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(5.dp)
                    .padding(start = 5.dp)
                    .clickable { }
            )

            Row(
                modifier = Modifier
                    .background(SecondaryColor)
                    .height(60.dp)
                    .padding(2.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = { visible = !visible },
                    modifier = Modifier
                        .padding(5.dp)
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(PrimaryColor)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = White,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }

            }
        }
        AnimatedVisibility(visible) {
            SearchBar() { userSearch ->
                filterRequest(userSearch)
            }
        }
    }
}

@Composable
fun SearchBar(
    filterRequest: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Gray,
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(8.dp)
        ) {
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    filterRequest(searchText)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                maxLines = 1,
                label = {
                    Text(text = "Search")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = SecondaryColor,
                        modifier = Modifier.size(30.dp)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ThirdColor,
                    focusedLabelColor = SecondaryColor,
                    unfocusedBorderColor = SecondaryColor
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                ),
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun FoodListContent(
    navController: NavController,
    postsViewModel: PostsViewModel = viewModel(factory = PostViewModelFactory(PostRepository())),
    userRequest: String
) {
    // Exact real time posts from firestore
    when (val postsListResponse =
        postsViewModel.postsStateFlow.asStateFlow().collectAsState().value) {

        is Resource.Success<*> -> {
            val foodItems = postsListResponse.data as List<*>
            val filtered = foodItems.filter { userRequest.lowercase() in (it as Post).title.lowercase() }
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