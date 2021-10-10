package com.example.foodvillage2205.util

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ReadMoreModifier() : Modifier {
    return Modifier
        .padding(top = 10.dp)
        .size(width = 110.dp, height = 35.dp)
        .clip(RoundedCornerShape(7.dp))
}