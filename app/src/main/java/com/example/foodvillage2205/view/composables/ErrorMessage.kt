package com.example.foodvillage2205.view.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ErrorMessage(errorMessage: String, modifier: Modifier = Modifier) {
    if (errorMessage.isNotEmpty()) {
        Text(
            text = errorMessage, color = Color.Red,
            textAlign = TextAlign.Center,
            modifier = modifier,
        )
    }
}