package com.example.foodvillage2205.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.foodvillage2205.R
import com.example.foodvillage2205.view.navigation.Route
import com.example.foodvillage2205.view.theme.PrimaryColor
import com.example.foodvillage2205.view.theme.SecondaryColor
import com.example.foodvillage2205.view.theme.White

@Composable
fun DetailScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBarDetail(navController)},
        content = {
            FoodDetailList()
        }
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
        verticalAlignment = Alignment.CenterVertically) {
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
            text= "Detail",
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
fun FoodDetailList() {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconTest()
        FoodDetail(        )

        Spacer(modifier = Modifier.padding(bottom = 80.dp))
    }
}

@Composable
fun IconTest() {
    Row(
        modifier = Modifier
            .padding(horizontal = 85.dp)
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_icon),
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
            text = "John Smith",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
            color = PrimaryColor,
        )
    }
}

@Composable
fun FoodDetail() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Image(
            painter = painterResource(id = R.drawable.spaghetti),
            contentDescription = "header",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(160.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        Text(
            text = "Spaghetti",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
            color = PrimaryColor,
        )
        Text(
            text = "Individually sized portions:" +
                    "\n- Spaghetti: Red Sauce with long thin noodles" +
                    "\n- Pesto Linguine: Basil Sauce with thick long noodles" +
                    "\n - Penne Alle Vodka: Red mild sauce with short noodles",
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
        Text(
            text = "604-273-3434",
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
        Text(
            text = "Italian Kitchen\n" +
                    "860 Burrard",
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
            modifier = Modifier.padding(bottom = 5.dp)
        )
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