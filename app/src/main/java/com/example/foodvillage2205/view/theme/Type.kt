package com.example.foodvillage2205.view.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.foodvillage2205.R

val Roboto = FontFamily(
    Font(R.font.robotoslab_medium, FontWeight.Normal),
    Font(R.font.robotoslab_light, FontWeight.Light),
    Font(R.font.robotoslab_bold, FontWeight.Bold),

)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
)