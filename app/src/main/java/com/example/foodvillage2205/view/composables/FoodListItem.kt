package com.example.foodvillage2205.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.screens.RobotoSlab
import com.example.foodvillage2205.view.theme.PrimaryColor
import com.example.foodvillage2205.view.theme.White

@Composable
fun FoodListItem(listItem: Post, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(210.dp)
            .width(100.dp)
            .clickable {
                navController.navigate(Route.DetailScreen.route + "/${listItem.id}")
            },
        elevation = 9.dp,
        backgroundColor = White,
        shape = RoundedCornerShape(corner = CornerSize(15.dp))
    ) {
        val painter = rememberImagePainter(listItem.imageUrl)

        Column {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,// Fit width
                modifier = Modifier
                    .height(125.dp)
                    .fillMaxWidth()
            )
            Text(
                text = listItem.title,
                fontWeight = FontWeight.Bold,
                fontFamily = RobotoSlab,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp),
                color = PrimaryColor,
                maxLines = 2,
            )

            // {Todo: Time Stamp in each post}
            Text(
                text = "VIEW DETAIL",
                fontWeight = FontWeight.Normal,
                fontFamily = RobotoSlab,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 5.dp, start = 10.dp),
            )
        }
    }
}