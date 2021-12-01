/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-14 02:22:48
 * @ Description: This is a reusable topbar.
 */

package com.v2205.foodvillage3.view.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.v2205.foodvillage3.view.screens.RobotoSlab
import com.v2205.foodvillage3.view.theme.PrimaryColor
import com.v2205.foodvillage3.view.theme.SecondaryColor
import com.v2205.foodvillage3.view.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun TopBar(
    navController: NavController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    text: String = "Food Village",
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
                text = text,
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
        AnimatedVisibility(visible) { SearchBar() { userSearch -> filterRequest(userSearch) } }
    }
}
